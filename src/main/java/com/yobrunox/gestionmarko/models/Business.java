package com.yobrunox.gestionmarko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;
    //@Column(length = 300)
    private String description;
    @Column(length = 11)
    private String ruc;
    @Column(length = 300)
    private String address;
    @Column(length = 50)
    private String phone;

    private String urlImage;


    private String message = "¡GRACIAS POR SU COMPRA!";
    //Users
    /*
    @OneToOne
    @JoinColumn(name = "User_idUser")
    private UserEntity user;
    */
    @OneToMany(mappedBy = "business",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserEntity> users;


    //Categoria
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "business",cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Category> categories;


    //Settings
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "business",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Settings> settings;

    //Report
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "business",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Report> reports;

    //Product
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "business",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

}