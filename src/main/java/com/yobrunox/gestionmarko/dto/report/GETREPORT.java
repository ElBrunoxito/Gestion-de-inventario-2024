package com.yobrunox.gestionmarko.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETREPORT <T>{
    private UUID idReport;
    private Integer type;
    private T report;
}
