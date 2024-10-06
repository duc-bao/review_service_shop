package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.ServiceBaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection = "services")
public class ServiceModel extends ServiceBaseModel {
}
