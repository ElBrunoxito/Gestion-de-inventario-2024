package com.yobrunox.gestionmarko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarginSaleBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Sale_idSale")
    @JsonIgnore
    private DetailSale detailSale;

    @ManyToOne
    @JoinColumn(name = "Buy_idBuy")
    @JsonIgnore
    private DetailBuy detailBuy;

    private Double quantity; // Cantidad utilizada de esta compra

}
