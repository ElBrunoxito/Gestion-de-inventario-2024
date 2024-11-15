package com.yobrunox.gestionmarko.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movements {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idMovement;


    //Product
    @ManyToOne
    @JoinColumn(name = "Product_idProduct")
    private Product product;

    //TypeMovement
    @ManyToOne
    @JoinColumn(name = "TypeMovement_idTypeMovement")
    private TypeMovement typeMovement;

    //Detail sale
    @OneToOne
    @JoinColumn(name = "Detail_Buy_idDetailBuy")
    private DetailBuy detailBuy;
    //Detail buy
    @OneToOne
    @JoinColumn(name = "Detail_Sale_idDetailSale")
    private DetailSale detailSale;

}
