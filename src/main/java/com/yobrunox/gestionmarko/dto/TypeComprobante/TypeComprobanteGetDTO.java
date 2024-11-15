package com.yobrunox.gestionmarko.dto.TypeComprobante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeComprobanteGetDTO {
    private Integer idTypeComprobante;
    private String name;
}
