package com.yobrunox.gestionmarko.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateWithPriceDTO {
    Double totalPrice;
    Date date;
}
