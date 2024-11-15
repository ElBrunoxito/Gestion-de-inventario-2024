package com.yobrunox.gestionmarko.dto.buy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyWithDetailsGetDTO {

    private UUID idBuy;
    private String nroBuy;
    private LocalDateTime buyUpdateDate;
    private Double buyTotal;
    private Integer idTypeComprobante;
    List<DetailBuyGetDTO> buys;
}
