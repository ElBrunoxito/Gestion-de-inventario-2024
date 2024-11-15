package com.yobrunox.gestionmarko.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypePayment;

    //YAPE - EFECTIVO
    private String name;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "typePayment",cascade = CascadeType.ALL)
    private List<Collect> collections;

}
