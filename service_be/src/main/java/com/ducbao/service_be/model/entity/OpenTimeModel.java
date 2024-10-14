package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.OpenTimeBaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "openTimes")
public class OpenTimeModel extends OpenTimeBaseModel {
}
