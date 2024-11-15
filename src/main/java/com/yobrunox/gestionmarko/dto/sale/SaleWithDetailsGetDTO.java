package com.yobrunox.gestionmarko.dto.sale;


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
public class SaleWithDetailsGetDTO {
    private UUID idSale;
    private String code;
    private LocalDateTime saleUpdateDate;
    private Double saleTotal;

    private Integer idTypeComprobante;

    private UUID idCollection;


    List<DetailSaleGetDTO> saleDetails;
}
