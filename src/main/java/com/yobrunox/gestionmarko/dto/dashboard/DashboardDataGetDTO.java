package com.yobrunox.gestionmarko.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDataGetDTO {

    //Buy
    private Double moneyTodayBuy;
    private Double totalBuyWeek;

    //Sale
    private Double cashTodaySale;
    private Double moneyYapeTodaySale;
    private Double totalSale;

    private Double totalSaleWeek;




    //Grafico sales and buys
    List<DateWithPriceDTO> sales;
    List<DateWithPriceDTO> buys;


    //
    private Long totalProducts;
    private Long totalCategories;
    private Long totalUnits;
}
