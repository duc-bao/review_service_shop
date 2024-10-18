package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.CategoryEnums;
import com.ducbao.common.model.enums.StateServiceEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class CategoryBaseModel extends BaseModel {
    @Id
    private String id;

    private String name;

    private CategoryEnums type;

    private String parentId;

    private String description;

    private boolean isDelete;
}
