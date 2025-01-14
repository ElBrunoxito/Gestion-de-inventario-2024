package com.yobrunox.gestionmarko.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBuyDataForReport {
    Integer sumQuantity;
    Double sumTotal;
}


