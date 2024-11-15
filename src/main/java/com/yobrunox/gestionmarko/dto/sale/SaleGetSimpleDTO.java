package com.yobrunox.gestionmarko.dto.sale;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleGetSimpleDTO {
    UUID idSale;
    String code;
}
