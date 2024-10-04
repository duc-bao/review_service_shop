package com.ducbao.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto <T>{
    private boolean success;
    private String message;
    private T data;
    private String statusCode;
    private MetaData meta;
}
