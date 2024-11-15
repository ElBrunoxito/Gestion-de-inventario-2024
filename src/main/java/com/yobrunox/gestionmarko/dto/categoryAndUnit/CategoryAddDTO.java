package com.yobrunox.gestionmarko.dto.categoryAndUnit;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAddDTO {

    private Long id;
    private String name;

    private Long idBusiness;
}
