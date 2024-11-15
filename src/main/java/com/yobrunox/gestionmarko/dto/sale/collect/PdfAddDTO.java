package com.yobrunox.gestionmarko.dto.sale.collect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfAddDTO {

    private String urlImage;
    @NotNull
    @NotBlank
    private String nameBusiness;
    @NotNull
    @NotBlank
    private String addressBusiness;
    @NotNull
    @NotBlank
    private String phoneBusiness;
    @NotNull
    @NotBlank
    private String code;
    //private String

    private String customer;
    private String address;
    private Date dateCollect;
    private String moneyType;


    //Products
    @NotNull
    @NotBlank
    List<ProductsPDFAddDTO> products;


    private Double discountAmount;


    @NotNull
    @NotBlank
    private Double total;

    private String message;


}
