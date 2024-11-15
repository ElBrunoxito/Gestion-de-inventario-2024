package com.yobrunox.gestionmarko.dto.buy;

import com.yobrunox.gestionmarko.models.Buy;
import com.yobrunox.gestionmarko.models.Product;
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
public class DetailBuyAddDTO {

    private UUID idDetailBuy;
    private Integer quantity;
    private Double price;
    private LocalDateTime dueDate;

    //MOVEMENTS

    //BUY
    private Buy buy;
    //PRODUCT
    private UUID idProduct;


}
