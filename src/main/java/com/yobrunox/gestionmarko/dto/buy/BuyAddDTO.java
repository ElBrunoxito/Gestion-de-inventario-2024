package com.yobrunox.gestionmarko.dto.buy;

import com.yobrunox.gestionmarko.models.DetailBuy;
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
public class BuyAddDTO {

    private UUID idBuy;
    private String nroBuy;
    //private LocalDateTime buyDate;
    //private LocalDateTime buyUpdateDate;
    //private Double buyTotal;

    private Integer idTypeComprobante;

    private UserEntity user;

    private List<DetailBuyAddDTO> detailsBuy;
}
