package com.yobrunox.gestionmarko.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateReportGetDTO {

    private String typeEncabezado;

    private LocalDateTime creationDate;

    private LocalDateTime startReport;
    private LocalDateTime endReport;

    private Integer quantityBuy;
    private Double totalBuy;

    private List<GetSalesDataForReportDTO> salesData;
    private Double totalSales;

    private Double sumDiscounts;


    private Double margen;
}
