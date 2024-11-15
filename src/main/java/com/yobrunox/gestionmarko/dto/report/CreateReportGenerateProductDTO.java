package com.yobrunox.gestionmarko.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportGenerateProductDTO {

    private String title;
    private UUID idProduct;
    private Integer idCategory;
    private Integer typeFilter; // 1 Solo ventas 2 Solo Solo cobros  3 Ambos

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
