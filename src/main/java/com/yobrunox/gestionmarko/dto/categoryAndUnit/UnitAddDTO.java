package com.yobrunox.gestionmarko.dto.categoryAndUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnitAddDTO {
    private Integer id;
    private String name;

}
