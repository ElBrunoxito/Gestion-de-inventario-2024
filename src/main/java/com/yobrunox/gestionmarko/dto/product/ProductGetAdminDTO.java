package com.yobrunox.gestionmarko.dto.product;

import com.yobrunox.gestionmarko.dto.categoryAndUnit.CategoryAddDTO;
import com.yobrunox.gestionmarko.dto.categoryAndUnit.UnitAddDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetAdminDTO {

    private UUID idProduct;
    private String barCode;
    private String description;
    private Long initialStock = 0L;
    private String category;
    private String unit;
}
