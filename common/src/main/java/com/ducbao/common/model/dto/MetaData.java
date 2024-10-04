package com.ducbao.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    private long total;
    private int totalPage;
    private int currentPage;
    private int pageSize;
}
