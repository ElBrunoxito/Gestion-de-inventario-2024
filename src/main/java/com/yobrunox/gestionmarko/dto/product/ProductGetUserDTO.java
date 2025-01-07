package com.yobrunox.gestionmarko.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetUserDTO {



    private UUID idProduct;
    private String barCode;
    private String description;
    private Double priceBuy;
    private Double priceSale;


}
