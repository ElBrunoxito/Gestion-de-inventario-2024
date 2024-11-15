package com.yobrunox.gestionmarko.dto.sale;

import com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailSaleGetDTO {
    public DetailSaleGetDTO(UUID idDetailBuy/*, String description*/, Integer quantity, Double price) {
        this.idDetailSale = idDetailBuy;
        //this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    private UUID idDetailSale;
    //private String description;
    private Integer quantity;
    private Double price;
    //private LocalDateTime dueDate;
    private ProductGetUserDTO product;
}
