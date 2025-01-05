package com.ducbao.service_be.service.elk;


import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ShopSearchRequest;
import com.ducbao.service_be.model.dto.response.ShopSearchResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShopSearchService {
   ResponseEntity<ResponseDto<List<ShopSearchResponse>>> searchShopService(ShopSearchRequest shopSearchRequest);
}
