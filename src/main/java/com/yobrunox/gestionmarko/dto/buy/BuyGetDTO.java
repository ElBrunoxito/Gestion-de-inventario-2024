package com.yobrunox.gestionmarko.dto.buy;

import com.yobrunox.gestionmarko.models.TypeComprobante;
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
public class BuyGetDTO {
    private UUID idBuy;
    private String nroBuy;
    private LocalDateTime buyUpdateDate;
    private com.yobrunox.gestionmarko.models.TypeComprobante.TYPE TypeComprobante;
    private Double buyTotal;
}
