package com.yobrunox.gestionmarko.dto.sale;


import com.yobrunox.gestionmarko.dto.buy.DetailBuyAddDTO;
import com.yobrunox.gestionmarko.models.DetailSale;
import com.yobrunox.gestionmarko.models.UserEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleAddDTO {
    private UUID idSale;
//    private String code;

    //private Double saleTotal;

    private Integer idTypeComprobante;

    private UserEntity user;

    private List<DetailSaleAddDTO> detailsSale;
}
