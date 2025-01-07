package com.yobrunox.gestionmarko.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailSale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idDetailSale;


    private Integer quantity;
    private Double price;


    @OneToOne(fetch = FetchType.LAZY,mappedBy = "detailSale",cascade = CascadeType.ALL)
    private Movements movements;

    //Buy
    @ManyToOne
    @JoinColumn(name = "Sale_idSale")
    private Sale sale;

    //Product
    @ManyToOne
    @JoinColumn(name = "Product_idProduct")
    private Product product;



    //MARGIN
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<MarginSaleBuy> saleBuyRelations;
}