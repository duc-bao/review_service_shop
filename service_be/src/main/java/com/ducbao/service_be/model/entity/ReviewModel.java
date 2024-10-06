package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.ReviewBaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "reviews")
public class ReviewModel extends ReviewBaseModel {
}
