package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.dashboard.DashboardDataGetDTO;
import com.yobrunox.gestionmarko.dto.dashboard.DateWithPriceDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.TypePayment;
import com.yobrunox.gestionmarko.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {
    private final SaleRepository saleRepository;
    private final BuyRepository buyRepository;
    private final TypePaymentRepository typePaymentRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;

    public DashboardService(SaleRepository saleRepository, BuyRepository buyRepository, TypePaymentRepository typePaymentRepository, ProductRepository productRepository, CategoryRepository categoryRepository, UnitRepository unitRepository) {
        this.saleRepository = saleRepository;
        this.buyRepository = buyRepository;
        this.typePaymentRepository = typePaymentRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.unitRepository = unitRepository;
    }
    //Ver dienro en compras de hoy
    //Ver dienro en compras de la semana


    //Ver dinero en efectivo de hoy
    //Ver dinero en yape de hoy
    //Ver totla en dienro de hoy

    //Ver dinero total en ventas de la semana


    //Tabmien necesitamos ver el un grafico entre compras y ventas, tipo lineas
    // Necestiamos ver los productos totales
    // Ver las categorias
    // Ver las unidades




    private List<DateWithPriceDTO> getSalesThisMonth(){
        List<DateWithPriceDTO> salesThisMonth = saleRepository.getDashBoardForSales().orElseThrow(
                () -> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Error al generar sales for month")
        );
        return salesThisMonth;
    }

    private List<DateWithPriceDTO> getBuysThisMonth(){
        List<DateWithPriceDTO> buysThisMonth = buyRepository.getDashBoardForBuys().orElseThrow(
                () -> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Error al generar sales for month")
        );
        return buysThisMonth;
    }


    public DashboardDataGetDTO getDashboardData(){

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // Inicio del día actual
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);


        LocalDateTime startOfWeek = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) // Lunes actual o pasado
                .atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)) // Domingo actual o próximo
                .atTime(23, 59, 59);


        Double moneytodayBuy = buyRepository.getMoneyBuyINDATES(startOfDay,endOfDay);
        Double totalBuyWeek = buyRepository.getMoneyBuyINDATES(startOfWeek,endOfWeek);



        //YAPE ID: 1
        //EFETIVO ID: 2

        Double cashTodaySale = saleRepository.getMoneySaleINDATES(startOfDay,endOfDay,2);
        Double moneyYapeTodaySale = saleRepository.getMoneySaleINDATES(startOfDay,endOfDay,1);
        Double totalSaleToday = cashTodaySale + moneyYapeTodaySale;

        Double totalSaleWeekCASH = saleRepository.getMoneySaleINDATES(startOfWeek,endOfWeek,2);
        Double totalSaleWeekYAPE = saleRepository.getMoneySaleINDATES(startOfWeek,endOfWeek,1);

        Double totalSaleWeek = totalSaleWeekYAPE + totalSaleWeekCASH;


        //Counts

        Long countProducts = productRepository.findAll().stream().count();
        Long countCategories = categoryRepository.findAll().stream().count();
        Long countUnits = unitRepository.findAll().stream().count();
        System.out.println("AQUI 8");


        DashboardDataGetDTO dashboardDataGetDTO = DashboardDataGetDTO.builder()
                .moneyTodayBuy(moneytodayBuy)
                .totalBuyWeek(totalBuyWeek)

                .cashTodaySale(cashTodaySale)
                .moneyYapeTodaySale(moneyYapeTodaySale)
                .totalSale(totalSaleToday)

                .totalSaleWeek(totalSaleWeek)

                .sales(this.getSalesThisMonth())
                .buys(this.getBuysThisMonth())

                .totalProducts(countProducts)
                .totalCategories(countCategories)
                .totalUnits(countUnits)

                .build();
        System.out.println("AQUI 9");

        return dashboardDataGetDTO;
    }


}
