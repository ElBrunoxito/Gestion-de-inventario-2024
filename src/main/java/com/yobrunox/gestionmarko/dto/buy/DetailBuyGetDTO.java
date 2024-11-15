package com.yobrunox.gestionmarko.dto.buy;

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
public class DetailBuyGetDTO {
    public DetailBuyGetDTO(UUID idDetailBuy/*, String description*/, Integer quantity, Double price, LocalDateTime dueDate) {
        this.idDetailBuy = idDetailBuy;
        //this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.dueDate = dueDate;
    }
    private UUID idDetailBuy;
//    private String description;
    private Integer quantity;
    private Double price;
    private LocalDateTime dueDate;

    private ProductGetUserDTO product;
    //private UUID idProduct;
}
