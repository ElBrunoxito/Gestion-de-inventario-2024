package com.yobrunox.gestionmarko.dto.sale.collect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectGetDTO {
    UUID idCollect;
    Integer idTypePayment;
    Double subTotal;
    Double discount;
    Double total;
    String urlPdf;
}
