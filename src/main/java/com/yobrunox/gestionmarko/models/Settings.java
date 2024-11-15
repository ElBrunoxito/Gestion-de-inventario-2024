package com.yobrunox.gestionmarko.models;

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
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sKey",length = 100)
    private String key;
    @Lob
    private String value;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

}
