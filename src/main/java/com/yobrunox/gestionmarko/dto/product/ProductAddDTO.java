package com.yobrunox.gestionmarko.dto.product;

import com.yobrunox.gestionmarko.dto.categoryAndUnit.CategoryAddDTO;
import com.yobrunox.gestionmarko.dto.categoryAndUnit.UnitAddDTO;
import com.yobrunox.gestionmarko.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddDTO {




    private UUID idProduct;
    private String barCode;
    private String description;
    private Long initialStock = 0L;

    //CATEGORIA
    private CategoryAddDTO categoryAddFast;

    private UnitAddDTO unitAddFast;



}
