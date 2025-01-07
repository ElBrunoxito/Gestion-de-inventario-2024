package com.yobrunox.gestionmarko.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DetailBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idDetailBuy;

    private Integer quantity;
    private Double price;
    private LocalDateTime dueDate;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "detailBuy",cascade = CascadeType.ALL)
    private Movements movements;

    //Buy
    @ManyToOne
    @JoinColumn(name = "Buy_idBuy")
    @JsonIgnore
    private Buy buy;


    //Product
    @ManyToOne
    @JoinColumn(name = "Product_idProduct")
    @JsonIgnore
    private Product product;




    //MARGIN
    private Integer remainingQuantity; // Cantidad restante disponible



}
