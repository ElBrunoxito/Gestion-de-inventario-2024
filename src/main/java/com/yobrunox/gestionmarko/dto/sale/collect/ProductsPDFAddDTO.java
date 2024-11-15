package com.yobrunox.gestionmarko.dto.sale.collect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsPDFAddDTO {

    private Float cantidad;
    private String description;
    private Double price;
    //private Double discount;
    private Double total;

}
