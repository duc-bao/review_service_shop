package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.ReviewReactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(description = "Request body cho việc cập nhật cảm xúc")
@Data
public class ReviewReactionRequest {
    @Schema(description = "Loại cảm xúc",
    example = "LIKE",
    allowableValues = {"LIKE", "HELPFUL", "NOTLIKE"}
    ,required = true)
    private ReviewReactionType type;
    @Schema(description = "Xóa hay thêm cảm xúc(true- xóa cảm xúc, false-thêm cảm xúc",
    example = "false",
    required = true)
    private boolean remove;
}
