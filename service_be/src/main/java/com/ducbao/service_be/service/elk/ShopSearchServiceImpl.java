package com.ducbao.service_be.service.elk;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonData;
import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.entity.*;
import com.ducbao.common.model.enums.SortOrderEnums;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.ShopSearchRequest;
import com.ducbao.service_be.model.dto.response.CategoryResponse;
import com.ducbao.service_be.model.dto.response.OpenTimeResponse;
import com.ducbao.service_be.model.dto.response.ServiceResponse;
import com.ducbao.service_be.model.dto.response.ShopSearchResponse;
import com.ducbao.service_be.model.entity.*;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.*;
import com.ducbao.service_be.service.CategoryService;
import com.ducbao.service_be.service.OpenTimeService;
import com.ducbao.service_be.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopSearchServiceImpl implements ShopSearchService {
    private final ShopRepository shopRepository;
    private final ShopSearchRepository shopSearchRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final CommonMapper commonMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final OpenTimeService openTimeService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final OpenTimeRepository openTimeRepository;
    private final ServiceRepository serviceRepository;

    private List<String> listFieldKey = Arrays.asList("name", "description", "serviceSearchBaseModels.name", "categorySearchBaseModel.name");

    @Override
    public ResponseEntity<ResponseDto<List<ShopSearchResponse>>> searchShopService(ShopSearchRequest shopSearchRequest) {
        SearchRequest searchRequest = buildSearchQuery(shopSearchRequest);

        try {
            var response = elasticsearchClient.search(searchRequest, ShopSearchModel.class);
            List<ShopSearchModel> shopSearchResponseList = response.hits().hits()
                    .stream().map(hit -> hit.source()).collect(Collectors.toList());
            List<ShopSearchResponse> shopSearchResponses = extractShopSearchResult(shopSearchResponseList);
            MetaData metaData = MetaData.builder()
                    .totalPage((int) Math.ceil((double) response.hits().total().value() / shopSearchRequest.getSize()))
                    .total(response.hits().total().value())
                    .pageSize(shopSearchRequest.getSize())
                    .currentPage(shopSearchRequest.getPage())
                    .build();
            return ResponseBuilder.okResponse(
                    "Tìm kiếm cửa hàng thành công",
                    shopSearchResponses,
                    metaData,
                    StatusCodeEnum.SHOP1000
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<ShopSearchResponse> extractShopSearchResult(List<ShopSearchModel> shopSearchResponseList) {
        List<ShopSearchResponse> shopSearchResponses = new ArrayList<>();
        for (ShopSearchModel shopSearchModel : shopSearchResponseList) {
            ShopSearchResponse shopSearchResponse = commonMapper.map(shopSearchModel, ShopSearchResponse.class);
            CategoryResponse categoryResponse = commonMapper.map(shopSearchModel.getCategorySearchBaseModel(), CategoryResponse.class);
            if(shopSearchModel.getServiceSearchBaseModels() != null){
                List<ServiceResponse> serviceResponses =  shopSearchModel.getServiceSearchBaseModels().stream().map(
                        serviceSearchBaseModel -> commonMapper.map(serviceSearchBaseModel, ServiceResponse.class)
                ).collect(Collectors.toList());
                shopSearchResponse.setServiceResponses(serviceResponses);
            }
            if(shopSearchModel.getOpenTimeSearchBaseModels() != null){
                List<OpenTimeResponse> openTimeResponses = shopSearchModel.getOpenTimeSearchBaseModels().stream().map(
                        openTimeSearchBaseModel -> commonMapper.map(openTimeSearchBaseModel, OpenTimeResponse.class)
                ).collect(Collectors.toList());
                shopSearchResponse.setOpenTimeResponses(openTimeResponses);
            }
            shopSearchResponse.setCategoryResponse(categoryResponse);
            shopSearchResponses.add(shopSearchResponse);
        }
        return shopSearchResponses;
    }

    private SearchRequest buildSearchQuery(ShopSearchRequest shopSearchRequest) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        if (shopSearchRequest.getKeyword() != null && !shopSearchRequest.getKeyword().isEmpty()) {
            boolQuery.must(buildKeywordSearch(shopSearchRequest.getKeyword()))
                    .filter(buildFilterSearch(shopSearchRequest).build()._toQuery());
        }
        boolQuery.must(TermQuery.of(
                m -> m.field("isVery")
                        .value(true)
        )._toQuery());
        boolQuery.must(TermQuery.of(
                m -> m.field("statusShopEnums")
                        .value("ACTIVE")
        )._toQuery());
        String idUser = null;

        try {
            idUser = userService.userId();
        } catch (Exception e) {
            log.debug("User not authenticated, skipping user context");
        }
        final String idUs = idUser;
        if (idUs != null) {
            boolQuery.mustNot(
                TermQuery.of(t -> t.field("createBy")
                        .value(idUs))._toQuery()
            );
        }

//        FunctionScoreQuery.Builder functionQuery = new FunctionScoreQuery.Builder().query(boolQuery.build()._toQuery())
//                .functions(buildFuctionScore(shopSearchRequest));

        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                .index("shop")
                .query(boolQuery.build()._toQuery())
                .from(shopSearchRequest.getPage() * shopSearchRequest.getSize())
                .size(shopSearchRequest.getSize());
        if (shopSearchRequest.getSortField() != null && !shopSearchRequest.getSortField().isEmpty()) {
            addSort(searchRequestBuilder, shopSearchRequest);
        }
        return searchRequestBuilder.build();
    }

//    private List<FunctionScore> buildFuctionScore(ShopSearchRequest shopSearchRequest) {
//        List<FunctionScore> functionScores = new ArrayList<>();
//        functionScores.add(
//                FunctionScore.of(
//                        f -> f.fieldValueFactor(
//                                fv -> fv.field("countReview")
//                                        .factor(1.0)
//                                        .modifier(FieldValueFactorModifier.Log1p)
//                                        .missing(0.0)
//                        )
//                )
//        );
////        functionScores.add(
////                FunctionScore.of(
////                        f -> f.fieldValueFactor(
////                                fv -> fv.field("point")
////                                        .factor(2.0)
////                                        .modifier(FieldValueFactorModifier.Log1p)
////                                        .missing(0.0)
////                        )
////                )
////        );
//        if (isValidGeoSearch(shopSearchRequest)) {
//            functionScores.add(
//                    FunctionScore.of(f -> f.gauss(
//                                    g -> g.field("location").placement( builder -> builder
//                                            .origin(JsonData.of(
//                                                            shopSearchRequest.getLongitude() + "," + shopSearchRequest.getLatitude()))
//                                            .scale(JsonData.of("3km"))    // Distance where score starts to decay
//                                            .offset(JsonData.of("0km"))   // Perfect score within this distance
//                                            .decay(0.5)
//                            )
//                            ).weight(5.0)
//                    )
//            );
//        }
//        return functionScores;
//    }

//    private boolean isValidGeoSearch(ShopSearchRequest shopSearchRequest) {
//        return shopSearchRequest.getLatitude() != null && shopSearchRequest.getLongitude() != null && shopSearchRequest.getDistance() != null;
//    }
    private void addSort(SearchRequest.Builder searchRequestBuilder, ShopSearchRequest shopSearchRequest) {
        try {
            switch (shopSearchRequest.getSortField()) {
                case "name":
                    searchRequestBuilder.sort(s -> s
                            .field(f -> f
                                    .field("name.keyword")
                                    .order(getSortOrder(shopSearchRequest.getSortOrderEnums()))
                            )
                    );
                    break;
//                case "point":
//                    searchRequestBuilder.sort(s -> s
//                            .field(f -> f
//                                    .field("point")
//                                    .order(getSortOrder(shopSearchRequest.getSortOrderEnums()))
//                            )
//                    );
//                    break;
                case "createdAt":
                    searchRequestBuilder.sort(s -> s
                            .field(f -> f
                                    .field("createdAt")
                                    .order(getSortOrder(shopSearchRequest.getSortOrderEnums()))
                            )
                    );
                    break;
                case "countReview":
                    searchRequestBuilder.sort(s -> s
                            .field(f -> f
                                    .field("countReview")
                                    .order(getSortOrder(shopSearchRequest.getSortOrderEnums()))
                            )
                    );
                    break;
                default:
                    // Optional: log or handle unexpected sort field
                    log.warn("Unexpected sort field: {}", shopSearchRequest.getSortField());
            }
        } catch (Exception e) {
            log.error("Error adding sort to search request", e);
            // Optionally add a default sort
        }
    }

    // Existing method to convert enum to Elasticsearch sort order
    private SortOrder getSortOrder(SortOrderEnums sortOrder) {
        return sortOrder == SortOrderEnums.DESC ? SortOrder.Desc : SortOrder.Asc;
    }

    private BoolQuery.Builder buildFilterSearch(ShopSearchRequest shopSearchRequest) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        List<CategoryModel> categoryModels = new ArrayList<>();
        if (shopSearchRequest.getCategoryId() != null) {
            List<String> categoryNames = shopSearchRequest.getCategoryId().stream()
                    .map(categoryService::getCategory)
                    .filter(Objects::nonNull)
                    .map(CategoryModel::getName)
                    .collect(Collectors.toList());
            addListFilter(boolQuery, "categorySearchBaseModel.name", categoryNames);
        }

        // Filter by open time
        if (shopSearchRequest.getOpenTimeId() != null) {
            List<String> openTimes = shopSearchRequest.getOpenTimeId().stream()
                    .map(openTimeService::getOpenTimeModel)
                    .filter(Objects::nonNull)
                    .flatMap(openTime -> Stream.of(openTime.getOpenTime()))
                    .collect(Collectors.toList());
            addListFilter(boolQuery, "openTimeSearchBaseModels.openTime", openTimes);
        }

        // Filter by city and district
        if (shopSearchRequest.getCity() != null) {
            addListFilter(boolQuery, "city", List.of(shopSearchRequest.getCity()));
        }
        if (shopSearchRequest.getDistrict() != null) {
            addListFilter(boolQuery, "district", List.of(shopSearchRequest.getDistrict()));
        }

        if (shopSearchRequest.getScoreReview() != null) {
            addRangeFilter(boolQuery, "point", shopSearchRequest.getScoreReview());
        }
        // Filter by review score range
        return boolQuery;
    }

    private void addRangeFilter(BoolQuery.Builder boolQuery, String field, Double rangeValues) {
        if (rangeValues != null && rangeValues > 0.5 && rangeValues <= 5) {
            Double min = rangeValues == 5 ? rangeValues - 0.5 : rangeValues - 0.5;
            Double max = rangeValues == 5 ? rangeValues : rangeValues + 0.5;

            BoolQuery.Builder rangeQuery = new BoolQuery.Builder();
            if (min != null) {
                rangeQuery.must(RangeQuery.of(r -> r.field(field).gte(JsonData.of(min)))._toQuery());
            }
            if (max != null) {
                rangeQuery.must(RangeQuery.of(r -> r.field(field).lte(JsonData.of(max)))._toQuery());
            }
            boolQuery.must(rangeQuery.build()._toQuery());
        }
    }

    private void addListFilter(BoolQuery.Builder boolQuery, String field, List<String> values) {
        if (values != null && !values.isEmpty()) {
            BoolQuery.Builder listQuery = new BoolQuery.Builder();
            values.forEach(value ->
                    listQuery.should(MatchQuery.of(m -> m.field(field).query(value))._toQuery())
            );
            boolQuery.must(listQuery.build()._toQuery());
        }
    }


    /**
     * Create Query with keyword have field name shop, name category, name service
     * @Param keyword
     * */
    private Query buildKeywordSearch(String keyword) {
        NestedQuery categorySearchNested = NestedQuery.of(
                n -> n.path("categorySearchBaseModel")
                        .query(
                                q ->  q.term(
                                        t -> t.field("categorySearchBaseModel.name").value(keyword)
                                )
                        ).boost(0.3f)
        );

//        NestedQuery serviceNested = NestedQuery.of(
//                n -> n.path("serviceSearchBaseModels")
//                        .query(q -> q.match(
//                                m -> m
//                                        .field("serviceSearchBaseModels.name")
//                                        .query(keyword)
//                                        .operator(Operator.Or)
//                                        .fuzziness("AUTO")
//                        )).boost(0.2f)
//        );

        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(
                m -> m.fields(listFieldKey)
                        .query(keyword)
                        .operator(Operator.Or)
                            .fuzziness("AUTO")
                        .type(TextQueryType.BestFields)
                        .tieBreaker(0.3)
                        .fields(List.of(
                                "name^3",
                                "description^2"
                        ))
        );
        return DisMaxQuery.of(
                d -> d.queries(
                        multiMatchQuery._toQuery(),
                        categorySearchNested._toQuery()
//                        serviceNested._toQuery()
                ).tieBreaker(0.3)
        )._toQuery();
    }
    /**
     * Handles migrate data to mongodb to Elasticsearch
     * *
     */
    @PostConstruct
    public void migrateShopsToElasticsearch() {
        // Fetch all shops from the database
        List<ShopModel> shopServices = shopRepository.findAll();

        // Prepare a list to store Elasticsearch documents
        List<ShopSearchBaseModel> searchModels = new ArrayList<>();

        // Convert database entities to Elasticsearch search models
        for (ShopModel shopModel : shopServices) {
            ShopSearchBaseModel searchModel = convertToSearchModel(shopModel);
            searchModels.add(searchModel);
        }

        // Bulk index documents to Elasticsearch
        bulkIndexShops(searchModels);
    }

    private ShopSearchModel convertToSearchModel(ShopModel shopModel) {
        return ShopSearchModel.builder()
                .id(shopModel.getId())
                .name(shopModel.getName())
                .avatar(shopModel.getAvatar())
                .description(shopModel.getDescription())
                .email(shopModel.getEmail())
                .isVery(shopModel.isVery())
                .urlWebsite(shopModel.getUrlWebsite())
                .phoneNumber(shopModel.getPhone())
                .location(new GeoPoint(
                        shopModel.getLatitude().doubleValue(),
                        shopModel.getLongitude().doubleValue()
                ))
                .createBy(convertToUser(shopModel.getIdUser()).getId())
                .mediaUrls(shopModel.getMediaUrls())
                .countReview(shopModel.getCountReview())
                .city(shopModel.getCity())
                .ward(shopModel.getWard())
                .district(shopModel.getDistrict())
                .hasAnOwner(shopModel.isHasAnOwner())
                .createdAt(shopModel.getCreatedAt())  // Giữ nguyên kiểu Instant
                .updatedAt(shopModel.getUpdatedAt())
                .point(shopModel.getPoint())
                .statusShopEnums(shopModel.getStatusShopEnums())
                .stateServiceEnums(shopModel.getStateServiceEnums())
                .categorySearchBaseModel(convertCategory(shopModel.getIdCategory()))
                .openTimeSearchBaseModels(convertOpenTimes(shopModel.getListIdOpenTime()))
                .serviceSearchBaseModels(convertServices(shopModel.getId()))
                .build();
    }

    private UserModel convertToUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    private CategorySearchBaseModel convertCategory(String categoryId) {
        if (categoryId == null) return null;

        CategoryBaseModel categoryModel = categoryRepository.findById(categoryId)
                .orElse(null);

        if (categoryModel == null) return null;

        return CategorySearchBaseModel.builder()
                .id(categoryModel.getId())
                .name(categoryModel.getName())
                .description(categoryModel.getDescription())
                .idParent(categoryModel.getParentId())
                .isDelete(categoryModel.isDelete())
                .tags(categoryModel.getTags())
                .build();
    }

    private List<OpenTimeSearchBaseModel> convertOpenTimes(List<String> openTimeIds) {
        if (openTimeIds == null || openTimeIds.isEmpty()) return Collections.emptyList();

        return openTimeRepository.findAllById(openTimeIds).stream()
                .map(openTime -> OpenTimeSearchBaseModel.builder()
                        .id(openTime.getId())
                        .dayOfWeek(openTime.getDayOfWeekEnum())
                        .openTime(openTime.getOpenTime())
                        .closeTime(openTime.getCloseTime())
                        .isDayOff(openTime.isDayOff())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ServiceSearchBaseModel> convertServices(String shopId) {
        // Assuming you have a method to find services by shopId
        List<ServiceModel> serviceModels = serviceRepository.findAllByIdShop(shopId);

        if (serviceModels == null || serviceModels.isEmpty()) return Collections.emptyList();

        return serviceModels.stream()
                .map(service -> ServiceSearchBaseModel.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .description(service.getDescription())
                        .thumbnail(service.getThumbnail())
                        .mediaUrl(service.getMediaUrl())
                        .countReview(service.getCountReview())
                        .point(service.getPoint())
                        .price(service.getPrice())
                        .isDelete(service.isDelete())
                        .build())
                .collect(Collectors.toList());
    }


    private void bulkIndexShops(List<ShopSearchBaseModel> searchModels) {
        try {
            // Bulk index the documents

            IndexOperations indexOperations = elasticsearchOperations.indexOps(ShopSearchModel.class);

            // Ensure index exists
            if (!indexOperations.exists()) {
                indexOperations.create();
            }

            // Perform bulk indexing
            // Prepare bulk indexing requests
            List<IndexQuery> queries = searchModels.stream()
                    .map(model -> {
                        IndexQuery query = new IndexQuery();
                        query.setId(model.getId()); // Assuming `getId()` provides the document ID
                        query.setObject(model);
                        return query;
                    })
                    .toList();

            // Perform bulk indexing
            elasticsearchOperations.bulkIndex(queries, ShopSearchModel.class);

            log.info("Successfully migrated {} shops to Elasticsearch", searchModels.size());
        } catch (Exception e) {
            log.error("Error during bulk indexing of shops", e);
            throw new RuntimeException("Failed to migrate shops to Elasticsearch", e);
        }
    }
}
