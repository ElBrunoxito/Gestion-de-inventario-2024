package com.yobrunox.gestionmarko.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetViewReportDTO {
    private UUID idReport;
    private String title;
    private LocalDateTime creation;
    private String rangoDate;
}
