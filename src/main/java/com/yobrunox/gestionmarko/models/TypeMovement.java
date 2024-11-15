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
public class TypeMovement {
    public enum TYPE{
        IN,OUT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TYPE name;


    //Movements
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "typeMovement",cascade = CascadeType.ALL)
    private List<Movements> movements;


}
