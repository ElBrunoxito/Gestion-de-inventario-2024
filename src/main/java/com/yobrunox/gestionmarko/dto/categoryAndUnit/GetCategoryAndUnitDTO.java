package com.yobrunox.gestionmarko.dto.categoryAndUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryAndUnitDTO {
    Set<String> categories;
    Set<String> units;
}
