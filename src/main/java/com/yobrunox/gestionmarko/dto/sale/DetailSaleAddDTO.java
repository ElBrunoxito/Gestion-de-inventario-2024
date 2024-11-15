package com.yobrunox.gestionmarko.dto.sale;

import com.yobrunox.gestionmarko.models.Buy;
import com.yobrunox.gestionmarko.models.Sale;
import com.yobrunox.gestionmarko.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailSaleAddDTO {
    private UUID idDetailSale;
    private Integer quantity;
    private Double price;
    //private LocalDateTime dueDate;

    //MOVEMENTS

    //BUY
    private Sale sale;
    //PRODUCT
    private UUID idProduct;
}
