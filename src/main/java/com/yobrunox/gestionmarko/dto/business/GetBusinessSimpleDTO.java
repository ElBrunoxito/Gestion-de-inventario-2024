package com.yobrunox.gestionmarko.dto.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBusinessSimpleDTO {
    private String nombreNegocio;
    private String urlImage;
}
