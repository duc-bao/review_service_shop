package com.ducbao.service_be.service.elk;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.entity.*;
import com.ducbao.common.model.enums.SortOrderEnums;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.ShopSearchRequest;
import com.ducbao.service_be.model.dto.request.ShopSuggestRequest;
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
import java.text.Normalizer;
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
    private final FavoriteRepository favoriteRepository;

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

    @Override
    public ResponseEntity<ResponseDto<List<ShopSearchResponse>>> suggestShopService(ShopSuggestRequest request) {
        FunctionScoreQuery functionScoreQuery = buildShopQuery(request.getCheckType(), request);
        try {
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("shop")
                    .query(q -> q.functionScore(functionScoreQuery))
                    .from(request.getPage() * request.getSize())
                    .size(request.getSize())
                    .build();
            if (request.getSortField() != null && !request.getSortField().isEmpty()) {
                searchRequest = new SearchRequest.Builder()
                        .index("shop") // Same index name as above
                        .query(q -> q.functionScore(functionScoreQuery))
                        .from(request.getPage() * request.getSize())
                        .size(request.getSize())
                        .sort(s -> s.field(f -> f
                                .field(request.getSortField())
                                .order(request.getSortOrderEnums().getSortOrder())))
                        .build();
            }

            SearchResponse<ShopSearchModel> response = elasticsearchClient.search(searchRequest, ShopSearchModel.class);
            List<ShopSearchModel> shopSearchResponses = response.hits()
                    .hits().stream().map(
                            hit -> hit.source()
                    ).collect(Collectors.toList());
            List<ShopSearchResponse> shopSearchResponseList = extractShopSearchResult(shopSearchResponses);
            MetaData metaData = MetaData.builder()
                    .totalPage((int) Math.ceil((double) response.hits().total().value() / request.getSize()))
                    .total(response.hits().total().value())
                    .pageSize(request.getSize())
                    .currentPage(request.getPage())
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy danh sách gợi ý cửa hàng thành công",
                    shopSearchResponseList,
                    metaData,
                    StatusCodeEnum.SHOP1000
            );
        } catch (Exception e) {
            log.error("Error suggestShopService - {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi thực hiện đề xuất cửa hàng",
                    StatusCodeEnum.SHOP1005
            );
        }

    }


    private FunctionScoreQuery buildShopQuery(String checkType, ShopSuggestRequest request) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        boolQuery.must(TermQuery.of(
                m -> m.field("isVery").value(true)
        )._toQuery());
        boolQuery.must(
                TermQuery.of(
                        t -> t.value("ACTIVE").field("statusShopEnums")
                )._toQuery()
        );
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
        List<FunctionScore> functionScores = new ArrayList<>();
        if (checkType.equalsIgnoreCase("forme")) {
            // Tìm kiếm theo sở thích + khoảng cách
            functionScores.addAll(buildForMeScore(idUser, request));
        } else if (checkType.equalsIgnoreCase("forShop")) {
            functionScores.addAll(buildForShop(request));
        } else if (checkType.equalsIgnoreCase("forSearch")) {
            functionScores.addAll(buildForSearch(idUser, request));
        }


        return FunctionScoreQuery.of(
                f -> f.query(boolQuery.build()._toQuery())
                        .functions(functionScores)
                        .boostMode(FunctionBoostMode.Sum)
        );
    }

    /**
     * Build FuctionScore đề xuất dành cho trang chủ người dùng
     * * Ưu tiên: vị trí, cửa hàng yêu thích, đánh giá cao, lượt bình chọn cao
     */
    private List<FunctionScore> buildForMeScore(String userID, ShopSuggestRequest request) {
        List<FunctionScore> functionScores = new ArrayList<>();
        // 1. Ưu tiên vị trí (điểm cao nhất)
        if (request.getLongitude() != null && request.getLatitude() != null) {
            List<Object> userLocation = Arrays.asList(
                    request.getLatitude().doubleValue(),
                    request.getLongitude().doubleValue()
            );
            functionScores.add(
                    buildScoringFunction(
                            "location",
                            null,
                            10,
                            userLocation,
                            "5",
                            null,
                            null,
                            null
                    )
            );
        }

        // 2. Cửa hàng yêu thích của người dùng
        if (userID != null && !userID.isEmpty()) {
            List<String> favoriteShopIds = getFavoriteShopIds(userID);
            if (favoriteShopIds != null && !favoriteShopIds.isEmpty()) {
                // 3. Đề xuất cửa hàng dựa trên danh mục yêu thích
                List<String> favoriteCategories = getFavoriteCategories(favoriteShopIds);
                if (!favoriteCategories.isEmpty()) {
                    functionScores.add(buildScoringFunction(
                            "path-list",
                            "categorySearchBaseModel.name",
                            6, // Điểm số ưu tiên
                            favoriteCategories,
                            "categorySearchBaseModel", // Path đến nested field
                            null,
                            null,
                            null
                    ));
                }
            }
        }
        // Công thức tính điểm: (điểm * số lượng đánh giá) / (số lượng đánh giá + 1)
        // Công thức này giúp cân bằng giữa điểm cao và số lượng đánh giá thấp
        String ratingScript = "double point = doc['point'].value; long countReview = " +
                "doc['countReview'].value; return (point) / (countReview + 1);";
        functionScores.add(buildScoringFunction(
                "scripts",
                null,
                2,
                null,
                null,
                null,
                ratingScript,
                null
        ));
        // 4. Cửa hàng có nhiều lượt bình chọn
        // Sử dụng logarithm để giảm ảnh hưởng của số lượng đánh giá quá lớn
        functionScores.add(buildScoringFunction(
                "scripts",
                null,
                2,
                List.of(2), // Số lượt bình chọn tối thiểu
                null,
                null,
                "long countReview = doc['countReview'].value; return countReview >= params.minReviews ? Math.log10(countReview) : 0.5",
                "minReviews"
        ));
        return functionScores;
    }


    // Trích suất danh muc theo sở thích của người dùng
    private List<String> getFavoriteCategories(List<String> favoriteShopIds) {
        try {
            BoolQuery.Builder boolQuery = new BoolQuery.Builder();
            // 2. Thêm điều kiện "must" với truy vấn TermsQuery để lọc các cửa hàng có ID nằm trong danh sách favoriteShopIds
            boolQuery.must(
                    TermsQuery.of(t -> t.field("id").terms(
                            TermsQueryField.of(f -> f.value(
                                    favoriteShopIds.stream()
                                            .map(FieldValue::of) // Chuyển đổi danh sách ID
                                            .collect(Collectors.toList())
                            ))
                    ))._toQuery()
            );
            SearchResponse<ShopSearchModel> searchResponse = elasticsearchClient.search(s -> s
                            .index("shop") // Chỉ mục Elasticsearch
                            .query(boolQuery.build()._toQuery()) // Truy vấn lấy tất cả dữ liệu
                            .size(favoriteShopIds.size()), // Giới hạn số lượng bản ghi trả về
                    ShopSearchModel.class // Kiểu dữ liệu ánh xạ kết quả
            );
            return searchResponse.hits().hits()
                    .stream().map(
                            hit -> hit.source()
                    ).filter(
                            shop -> shop != null && shop.getCategorySearchBaseModel() != null
                    ).map(
                            s -> s.getCategorySearchBaseModel().getName()
                    ).distinct().collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getFavoriteCategories - {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * Build đề xuất tính điểm dành cho cửa hàng
     * Ưu tiên: cửa hàng tương tự, vị trí, đánh giá cao, lượt bình chọn
     */
    private List<FunctionScore> buildForShop(ShopSuggestRequest request) {
        List<FunctionScore> functionScores = new ArrayList<>();
        if (request.getIdShop() != null && !request.getIdShop().isEmpty()) {
            // Lấy danh mục của cửa hàng hiện tại (giả sử có phương thức hỗ trợ)
            List<String> shopCategories = getFavoriteCategories(List.of(request.getIdShop()));
            if (shopCategories != null && !shopCategories.isEmpty()) {
                functionScores.add(buildScoringFunction(
                        "path-list",
                        "categorySearchBaseModel.name",
                        20, // Điểm số ưu tiên
                        shopCategories,
                        "categorySearchBaseModel", // Path đến nested field
                        null,
                        null,
                        null
                ));
            }

        }
        // 2. Ưu tiên vị trí nếu có
        if (request.getLatitude() != null && request.getLongitude() != null) {
            List<Object> userLocation = Arrays.asList(
                    request.getLatitude().doubleValue(),
                    request.getLongitude().doubleValue()
            );

            functionScores.add(buildScoringFunction(
                    "location",
                    null,
                    10,
                    userLocation,
                    "15", // Khoảng cách tối đa 15km cho cửa hàng tương tự
                    null,
                    null,
                    null
            ));
        }
        // 3. Cửa hàng có đánh giá cao
        String ratingScript = "double point = doc['point'].value; long countReview = " +
                "doc['countReview'].value; return (point) / (countReview + 1);";
        functionScores.add(buildScoringFunction(
                "scripts",
                null,
                2,
                null,
                null,
                null,
                ratingScript,
                null
        ));
        // 4. Cửa hàng có nhiều lượt bình chọn
        functionScores.add(buildScoringFunction(
                "scripts",
                null,
                2,
                List.of(2), // Số lượt bình chọn tối thiểu
                null,
                null,
                "long countReview = doc['countReview'].value; return countReview >= params.minReviews ? Math.log10(countReview) : 0.5",
                "minReviews"
        ));

        return functionScores;
    }

    /**
     * Build đề xuất dành cho thanh search
     * Ưu tiên: khớp từ khóa, vị trí, đánh giá cao, lượt bình chọn
     */
    private List<FunctionScore> buildForSearch(String userID, ShopSuggestRequest request) {
        List<FunctionScore> functionScores = new ArrayList<>();
        // 1. Khớp chính xác với tên cửa hàng (nếu có từ khóa tìm kiếm)
        // Lưu ý: Phần này thường được xử lý trong truy vấn chính, không ở FunctionScore
        // 2. Ưu tiên vị trí
        if (request.getLatitude() != null && request.getLongitude() != null) {
            List<Object> userLocation = Arrays.asList(
                    request.getLatitude().doubleValue(),
                    request.getLongitude().doubleValue()
            );

            functionScores.add(buildScoringFunction(
                    "location",
                    null,
                    10.0,
                    userLocation,
                    "10", // Khoảng cách tối đa 10km
                    null,
                    null,
                    null
            ));
        }
        // 3. Cửa hàng yêu thích của người dùng
        if (userID != null && !userID.isEmpty()) {
            functionScores.add(buildScoringFunction(
                    "path",
                    "favoriteShops.userId",
                    4,
                    List.of(userID),
                    "favoriteShops",
                    null,
                    null,
                    null
            ));
        }
        // 5. Cửa hàng có đánh giá cao
        functionScores.add(buildScoringFunction(
                "normal",
                "averageRating",
                3,
                List.of("4.0"),
                null,
                null,
                null,
                null
        ));
        // 6. Cửa hàng có nhiều lượt bình chọn
        functionScores.add(buildScoringFunction(
                "scripts",
                null,
                6.0,
                List.of("50"),
                null,
                null,
                "doc['reviewCount'].value >= params.minReviews ? Math.log10(doc['reviewCount'].value) : 0.5",
                "minReviews"
        ));
//        // 7. Cửa hàng phổ biến (có nhiều lượt truy cập)
//        functionScores.add(buildScoringFunction(
//                "normal",
//                "popularityScore",
//                5.0,
//                List.of("high"),
//                null,
//                null,
//                null,
//                null
//        ));
        return functionScores;
    }

    /**
     * Phương thức hỗ trợ tính điểm score dựa vào các loại tính điểm khác nhau
     */
    private <T> FunctionScore buildScoringFunction(String typeScore, String field, double score, List<T> data, String path,
                                                   String sufix, String script, String param) {
        switch (typeScore) {
            case "normal":
                return FunctionScore.of(fs -> fs.filter(
                        MatchQuery.of(m -> m.field(field).query(data.get(0).toString()))._toQuery()
                ).weight(score));
            case "list":
                BoolQuery.Builder builder = new BoolQuery.Builder();
                data.forEach(item -> builder.should(
                        MatchQuery.of(m -> m.field(field).query(item.toString()))._toQuery()
                ));
                return FunctionScore.of(
                        fs -> fs.filter(builder.build()._toQuery()).weight(score)
                );
            case "path-list":
                BoolQuery.Builder plistQuery = new BoolQuery.Builder();
                data.forEach(item -> plistQuery.should(NestedQuery.of(n -> n
                                .path(path)
                                .query(MatchQuery.of(r -> r
                                                .field(field)
                                                .query(item.toString()))
                                        ._toQuery()))
                        ._toQuery()));
                return FunctionScore.of(
                        fs -> fs.filter(plistQuery.build()._toQuery()).weight(score)
                );
            case "path":
                return FunctionScore.of(
                        fs -> fs.filter(
                                NestedQuery.of(n -> n.path(path).query(
                                        MatchQuery.of(r -> r.field(field).query(data.get(0).toString()))._toQuery()
                                ))._toQuery()).weight(score));
            case "path_range":
                BoolQuery.Builder prangeQuery = new BoolQuery.Builder();
                prangeQuery.should(
                        NestedQuery.of(n -> n.path(path).query(
                                BoolQuery.of(b -> b.must(RangeQuery.of(r -> r.field(field + ".min" + sufix).lte(JsonData.of(data.get(0))))._toQuery())
                                        .must(RangeQuery.of(r -> r.field(field + ".max" + sufix).gte(JsonData.of(data.get(0))))._toQuery())
                                )._toQuery()
                        ))._toQuery());
                return FunctionScore.of(
                        fs -> fs.filter(prangeQuery.build()._toQuery()).weight(score)
                );
            case "scripts":
                if (param != null && data != null && !data.isEmpty()) {
                    // Trường hợp có tham số
                    return FunctionScore.of(
                            fs -> fs.scriptScore(ss -> ss.script(
                                    s -> s.inline(v -> v.source(script).params(param, JsonData.of(data.get(0))))
                            )).weight(score)
                    );
                } else {
                    // Trường hợp không có tham số
                    return FunctionScore.of(
                            fs -> fs.scriptScore(ss -> ss.script(
                                    s -> s.inline(v -> v.source(script))
                            )).weight(score)
                    );
                }
            // XỬ dụng công thức Haversine tính khoảng cách
            case "location":
                // Giả sử data[0] là latitude, data[1] là longitude
                if (data.size() >= 2) {
                    String scriptString = "double distance = doc['location'].arcDistance(params.lat, params.lon) / 1000;" +
                            "double maxDistance = params.maxDistance;" +
                            "return distance <= maxDistance ? (maxDistance / (distance + 1)) : 1;";
                    double maxDistance = path != null ? Double.parseDouble(path) : 10.0; // 🟢 Chuyển `path` thành số
                    Map<String, JsonData> params = new HashMap<>();
                    params.put("lat", JsonData.of(Double.parseDouble(data.get(0).toString())));
                    params.put("lon", JsonData.of(Double.parseDouble(data.get(1).toString())));
                    params.put("maxDistance", JsonData.of(maxDistance));

                    return FunctionScore.of(fs -> fs
                            .scriptScore(ScriptScoreFunction.of(scriptFn -> scriptFn
                                    .script(s -> s
                                            .inline(i -> i
                                                    .source(scriptString)
                                                    .params(params)
                                            )
                                    )
                            ))
                            .weight(score)
                    );
                }
                // Trường hợp không đủ dữ liệu vị trí
                return FunctionScore.of(fs -> fs.weight(1.0));
            default:
                throw new IllegalArgumentException("Invalid typeScore: " + typeScore);
        }
    }

    // Các phương thức hỗ trợ (cần được implement tùy theo thiết kế hệ thống)
    private List<String> getShopCategories(String shopId) {
        // TODO: Implement logic to get categories of the shop
        // Đây là phương thức giả định, bạn cần implement theo cách của mình
        return new ArrayList<>();
    }

    private String getShopRegion(String shopId) {
        // TODO: Implement logic to get region of the shop
        // Đây là phương thức giả định, bạn cần implement theo cách của mình
        return "";
    }

    /**
     * Phương thức tìm kiếm dựa trên các yêu cầu
     *
     * @param shopSearchRequest
     * @return searchRequest
     */
    private SearchRequest buildSearchQuery(ShopSearchRequest shopSearchRequest) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        if (shopSearchRequest.getKeyword() != null && !shopSearchRequest.getKeyword().isEmpty()) {
            boolQuery.must(buildKeywordSearch(shopSearchRequest.getKeyword()));
        }

        boolQuery.filter(buildFilterSearch(shopSearchRequest).build()._toQuery());

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

    private List<ShopSearchResponse> extractShopSearchResult(List<ShopSearchModel> shopSearchResponseList) {
        List<ShopSearchResponse> shopSearchResponses = new ArrayList<>();
        for (ShopSearchModel shopSearchModel : shopSearchResponseList) {
            ShopSearchResponse shopSearchResponse = commonMapper.map(shopSearchModel, ShopSearchResponse.class);
            CategoryResponse categoryResponse = commonMapper.map(shopSearchModel.getCategorySearchBaseModel(), CategoryResponse.class);
            if (shopSearchModel.getServiceSearchBaseModels() != null) {
                List<ServiceResponse> serviceResponses = shopSearchModel.getServiceSearchBaseModels().stream().map(
                        serviceSearchBaseModel -> commonMapper.map(serviceSearchBaseModel, ServiceResponse.class)
                ).collect(Collectors.toList());
                shopSearchResponse.setServiceResponses(serviceResponses);
            }
            if (shopSearchModel.getOpenTimeSearchBaseModels() != null) {
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

    private void addSort(SearchRequest.Builder searchRequestBuilder, ShopSearchRequest shopSearchRequest) {
        try {
            switch (shopSearchRequest.getSortField()) {
                case "name":
                    searchRequestBuilder.sort(s -> s
                            .field(f -> f
                                    .field("name")
                                    .order(getSortOrder(shopSearchRequest.getSortOrderEnums()))
                            )
                    );
                    break;
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
        }
    }

    // Existing method to convert enum to Elasticsearch sort order
    private SortOrder getSortOrder(SortOrderEnums sortOrder) {
        return sortOrder == SortOrderEnums.DESC ? SortOrder.Desc : SortOrder.Asc;
    }

    /**
     * Builds a Boolean query for shop search filtering based on the provided request parameters.
     *
     * @param shopSearchRequest The request containing search filter parameters
     * @return A BoolQuery.Builder with all applied filters
     */
    private BoolQuery.Builder buildFilterSearch(ShopSearchRequest shopSearchRequest) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        addCategoryFilter(boolQuery, shopSearchRequest.getCategoryId());
        addTermFilter(boolQuery, "city", shopSearchRequest.getCity());
        addTermFilter(boolQuery, "district", shopSearchRequest.getDistrict());
        applyScoreFilter(boolQuery, shopSearchRequest.getScoreReview());
        addCloseTimeFilter(boolQuery, shopSearchRequest.getCloseTime());
        return boolQuery;
    }

    private void addCloseTimeFilter(BoolQuery.Builder boolQuery, String closeTime) {
        if (closeTime == null || closeTime.isEmpty()) {
            return;
        }

        // Chuẩn hóa định dạng thời gian (thêm 0 phía trước nếu cần)
        final String closeTimes = normalizeTimeFormat(closeTime);

        // Sử dụng nested query để lọc theo điều kiện
        NestedQuery nestedQuery = NestedQuery.of(
                n -> n.path("openTimeSearchBaseModels")
                        .query(
                                q -> q.bool(
                                        b -> b.must(
                                                m -> m.term(
                                                        t -> t.field("openTimeSearchBaseModels.isDayOff")
                                                                .value(false)
                                                )
                                        ).must(
                                                m -> m.range(
                                                        r -> r.field("openTimeSearchBaseModels.closeTime")
                                                                .lte(JsonData.of(closeTimes))
                                                )
                                        )
                                )
                        )
        );

        // Đổi thành filter để tối ưu hiệu suất
        boolQuery.filter(nestedQuery._toQuery());
    }

    private void addTermFilter(BoolQuery.Builder boolQuery, String filed, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        String normalizedValue = Normalizer.normalize(value.trim(), Normalizer.Form.NFC);

        boolQuery.filter(
                TermQuery.of(
                        t -> t.field(filed).value(normalizedValue)
                )._toQuery()
        );
    }

    private void addCategoryFilter(BoolQuery.Builder boolQuery, List<String> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        NestedQuery nestedQuery = NestedQuery.of(n -> n
                .path("categorySearchBaseModel")
                .query(q -> q
                        .bool(b -> {
                            for (String categoryId : categoryIds) {
                                b.should(s -> s
                                        .bool(sb -> sb
                                                .should(s1 -> s1.term(t -> t
                                                        .field("categorySearchBaseModel.id") // Field id
                                                        .value(categoryId)
                                                ))
                                                .should(s2 -> s2.term(t -> t
                                                        .field("categorySearchBaseModel.idParent") // Field idParent
                                                        .value(categoryId)
                                                ))
                                        )
                                );
                            }
                            return b;
                        })
                )
        );
        boolQuery.should(nestedQuery._toQuery());
    }

    private void applyScoreFilter(BoolQuery.Builder boolQuery, Double score) {
        if (score != null && score > 0.5 && score <= 5) {
            Double min = score - 0.5;
            Double max = score == 5 ? score : score + 0.5;

            ScriptQuery scriptQuery = ScriptQuery.of(s -> s
                    .script(script -> script
                            .inline(inline -> inline
                                    .source("doc['totalScore'].value / doc['countReview'].value >= params.min && " +
                                            "doc['totalScore'].value / doc['countReview'].value <= params.max")
                                    .params("min", JsonData.of(min))
                                    .params("max", JsonData.of(max))
                            )
                    )
            );

            boolQuery.filter(scriptQuery._toQuery());
        }
    }

    // Hàm chuẩn hóa định dạng thời gian
    private String normalizeTimeFormat(String time) {
        if (time == null || time.isEmpty()) {
            return time;
        }

        // Kiểm tra nếu định dạng đã là HH:MM
        if (time.matches("\\d{2}:\\d{2}")) {
            return time;
        }

        // Nếu định dạng là H:MM
        if (time.matches("\\d:\\d{2}")) {
            return "0" + time;
        }

        // Nếu định dạng là HH:M
        if (time.matches("\\d{2}:\\d")) {
            return time.substring(0, 3) + "0" + time.substring(3);
        }

        // Nếu định dạng là H:M
        if (time.matches("\\d:\\d")) {
            return "0" + time.substring(0, 2) + "0" + time.substring(2);
        }

        return time;
    }

    /**
     * Create Query with keyword have field name shop, name category, name service
     *
     * @Param keyword
     */
    private Query buildKeywordSearch(String keyword) {
        Query categorySearchNested = NestedQuery.of(
                n -> n.path("categorySearchBaseModel")
                        .query(
                                q -> q.match(
                                        m -> m.field("categorySearchBaseModel.name")
                                                .query(keyword)
                                                .fuzziness("AUTO")  // Cho phép tìm kiếm mờ
                                                .operator(Operator.Or)  // Tìm kiếm theo từ khóa
                                )// Tăng trọng số cho trường này
                        ))._toQuery();
        Query multiMatchQuery = MultiMatchQuery.of(
                m -> m.fields(listFieldKey)
                        .query(keyword)
                        .operator(Operator.Or)
                        .type(TextQueryType.BestFields)
                        .tieBreaker(0.3)
        )._toQuery();

        return BoolQuery.of(b -> b.should(categorySearchNested, multiMatchQuery))._toQuery();
    }

    /**
     * Handles migrate data to mongodb to Elasticsearch
     * *
     */
//    @PostConstruct
//    public void migrateShopsToElasticsearch() {
//        // Fetch all shops from the database
//        List<ShopModel> shopServices = shopRepository.findAll();
//
//        // Prepare a list to store Elasticsearch documents
//        List<ShopSearchBaseModel> searchModels = new ArrayList<>();
//
//        // Convert database entities to Elasticsearch search models
//        for (ShopModel shopModel : shopServices) {
//            ShopSearchBaseModel searchModel = convertToSearchModel(shopModel);
//            searchModels.add(searchModel);
//        }
//
//        // Bulk index documents to Elasticsearch
//        bulkIndexShops(searchModels);
//    }

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

    /**
     * Lấy danh sách id cửa hàng yêu thích cửa người dùng
     */
    private List<String> getFavoriteShopIds(String userID) {
        List<FavoriteModel> favoriteModels = favoriteRepository.findAllByIdUser(userID);
        if (favoriteModels == null || favoriteModels.isEmpty()) {
            return null;
        }
        return favoriteModels.stream().map(
                FavoriteBaseModel::getIdShop
        ).collect(Collectors.toList());
    }

    /**
     * Lấy danh sách thể loại yêu thích
     */
//    private List<String> getFavoriteCategories(String userID) {
//
//    }
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

    /**
     * Tạo index với cửa hàng
     */
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
