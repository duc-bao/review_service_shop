package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Nội dung comment không được để trống")
    private String content;
}
