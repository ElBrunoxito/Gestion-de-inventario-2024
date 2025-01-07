package com.yobrunox.gestionmarko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProduct;

    private String barCode;
    @Column(unique = true,nullable = false)
    private String description;
    @Column(nullable = false)
    private Long initialStock;
    @Column(nullable = false)
    private Long currentStock;


    //Precios
    private Double priceBuy;
    private Double priceSale;

    //Indicators
    private Long minStock;
    private Long maxStock;


    //ACTIVO O INACTIVO
    private Boolean state;

    //category
    @ManyToOne
    @JoinColumn(name = "Category_id")
    @JsonIgnore
    private Category category;

    //business
    @ManyToOne
    @JoinColumn(name = "Business_idBusiness")
    @JsonIgnore
    private Business business;
    //Unit
    @ManyToOne
    @JoinColumn(name = "Unit_idUnit")
    @JsonIgnore
    private Unit unit;

    //Movements
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "product",cascade = CascadeType.ALL)
    private List<Movements> movements;

    //Detail Buy s
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "product",cascade = CascadeType.ALL)
    private List<DetailBuy> detailBuys;
    //Detail Sale s
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "product",cascade = CascadeType.ALL)
    private List<DetailSale> detailSales;







}
