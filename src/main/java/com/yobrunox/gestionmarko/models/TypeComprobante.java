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
public class TypeComprobante {
    public enum TYPE{
       BOLETA, FACTURA, TICKTET
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeComprobante;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private TYPE typeComprobante;


}
