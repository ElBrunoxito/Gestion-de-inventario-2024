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
public class Buy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idBuy;

    //Numero de comprobante
    private String nroBuy;

    private LocalDateTime buyDate;
    private LocalDateTime buyUpdateDate;

    private Double buyTotal;

    //TypeComprovante
    @ManyToOne
    @JoinColumn(name = "TypeComprobante_idComprobante")
    private TypeComprobante typeComprobante;

    @ManyToOne
    @JoinColumn(name = "User_idUser")
    @JsonIgnore
    private UserEntity user;

   // @ManyToOne
    //@JoinColumn(name = "Product_idProduct")
    //private Product product;

    //@OneToMany(fetch = FetchType.LAZY,mappedBy = "product",cascade = CascadeType.ALL)
    //private List<DetailBuy> detailBuys;

    @OneToMany(mappedBy = "buy",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DetailBuy> detailBuys;

}
