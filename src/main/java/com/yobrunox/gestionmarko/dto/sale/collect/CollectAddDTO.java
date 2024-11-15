package com.yobrunox.gestionmarko.dto.sale.collect;

import com.yobrunox.gestionmarko.models.Sale;
import com.yobrunox.gestionmarko.models.TypePayment;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectAddDTO {
    //private UUID idCollection;
    private Double discountAmount;
    //private Double amountCollection;
    //1 PAGADO //0 NO EXISTE pero mejor lo hacemos con la existencia de collect
    private Boolean stateCollection;
    //private String urlPdf;

    @NotNull
    private UUID idSale;

    private Integer idTypePayment;
}
