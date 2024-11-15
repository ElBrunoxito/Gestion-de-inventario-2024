package com.yobrunox.gestionmarko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collect {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCollection;

    private LocalDateTime creationCollectDate;

    private Double discountAmount;

    //Con todo descontado
    private Double amountCollection;

    private Boolean stateCollection;

    private String urlPdf;

    @OneToOne
    @JoinColumn(name = "Sale_idSale")
    @JsonIgnore
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "TypePayment_idTypePayment")
    private TypePayment typePayment;


}
