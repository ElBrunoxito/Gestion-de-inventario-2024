package com.yobrunox.gestionmarko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;

    private Boolean isCreator;


    //Tokens
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private List<Token> tokens;


    //roles
    /*@ManyToMany(fetch = FetchType.LAZY)*/
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class,cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    //ADMIN
    @ManyToOne
    @JoinColumn(name = "adminId",nullable = true)
    private UserEntity admin;

    //USERS
    @OneToMany(mappedBy = "admin",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<UserEntity> users;


    //Business
    @ManyToOne
    @JoinColumn(name = "Business_idBusiness")
    @JsonIgnore
    private Business business;


    //Buys
    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Buy> buys;
    //Sale
    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Sale> sales;











    //roles y autorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return List.of();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name())))
                .collect(Collectors.toSet());

    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }



}
