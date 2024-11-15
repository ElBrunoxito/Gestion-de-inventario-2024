package com.yobrunox.gestionmarko.dto.sale;

import com.yobrunox.gestionmarko.models.TypePayment;
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
public class SaleGetDTO {
    private UUID idSale;
    private String code;
    private LocalDateTime saleUpdateDate;
    private Double saleTotal;


    private Double descuento;

    private Double total;

    private Boolean cobrado;
    private String typePayment;

    private String user;

    private String urlPdf;

    //private




    //private com.yobrunox.gestionmarko.models.TypeComprobante.TYPE TypeComprobante;
    //private Double buyTotal;
}
