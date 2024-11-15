package com.yobrunox.gestionmarko.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idSale;
    ///TIKECT 1 - TICK2 - TICK3 - T4 - T5 - T6 - T7 - T8
    ///Generar TICKET

    //@GeneratedValue(strategy )
    private String code;

    /*
    @PrePersist
    private void generateCode(){
        if(this.code == null){
            this.code = "T" + this.id;
        }
    }*/

    private LocalDateTime saleDate;
    private LocalDateTime saleUpdateDate;



    private Double saleTotal;

    //TypeComprovante
    @ManyToOne
    @JoinColumn(name = "TypeComprobante_idComprobante")
    private TypeComprobante typeComprobante;

    @ManyToOne
    @JoinColumn(name = "User_idUser")
    private UserEntity user;

    /*
    @OneToOne
    @JoinColumn(name = "Collect_idCollection")
    private Collect collect;*/

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "sale",cascade = CascadeType.ALL)
    private Collect collect;


    @OneToMany(mappedBy = "sale",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<DetailSale> detailSales;



}
