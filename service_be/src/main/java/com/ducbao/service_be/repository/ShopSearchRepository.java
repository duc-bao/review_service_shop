package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ShopSearchModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopSearchRepository extends ElasticsearchRepository<ShopSearchModel, String> {
}
