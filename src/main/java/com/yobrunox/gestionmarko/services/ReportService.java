package com.yobrunox.gestionmarko.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.api.services.storage.Storage;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.report.*;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.models.DetailBuy;
import com.yobrunox.gestionmarko.models.Product;
import com.yobrunox.gestionmarko.models.Report;
import com.yobrunox.gestionmarko.repository.BusinessRepository;
import com.yobrunox.gestionmarko.repository.ProductRepository;
import com.yobrunox.gestionmarko.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {


    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;

    public GETREPORT generateReportByProduct(CreateReportGenerateProductDTO data, Long idBusiness){

        LocalDateTime startOfDay = data.getStartDate().with(LocalTime.MIN);  // 00:00:00 de hoy
        LocalDateTime endOfDay = data.getEndDate().with(LocalTime.MAX);






        /*GetBuyDataForReport buyReport = reportRepository.getBuysReportForProduct(idBusiness, startOfDay, endOfDay, data.getIdProduct()).orElseThrow(
                ()-> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Ha ocurrido un error al buscar buy in report")

        );*/
        List<DetailBuy> dbs = reportRepository.getBuysReportForProduct(idBusiness, startOfDay, endOfDay, data.getIdProduct());
        /*Integer sQ = 0;
        Double sT = 0;
        dbs.stream()
                .forEach(db->{
                    sQ = sQ db.getQuantity();
                    sT += db.getQuantity() * db.getPrice();
                });*/


        GetBuyDataForReport buyReport = dbs.stream()
                .reduce(
                        new GetBuyDataForReport(0,0.0),
                        (acc, item) -> new GetBuyDataForReport(
                                acc.getSumQuantity() + item.getQuantity(),
                                acc.getSumTotal() + item.getQuantity() * item.getPrice()
                        ),
                        (acc1, acc2) -> new GetBuyDataForReport(
                                acc1.getSumQuantity()+ acc2.getSumQuantity(),
                                acc1.getSumTotal() + acc2.getSumTotal()
                        )
                );

        List<GetSalesDataForReportDTO> salesReport = reportRepository.getSalesReportForProduct(idBusiness,startOfDay,endOfDay,data.getIdProduct(),data.getTypeFilter()).orElseThrow(
                ()-> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Ha ocurrido un error al buscar sales in report")
        );

        //List<GetSalesDataForReportDTO> salesReport = new ArrayList<>();

        Product product = productRepository.findById(data.getIdProduct()).orElse(null);

        Double totalSales = salesReport.stream()
                .mapToDouble(s->s.getQuantity()*s.getPrice())
                .sum();

        System.out.println("Aqui 9");


        GenerateReportGetDTO generateReportGetDTO = GenerateReportGetDTO.builder()
                .typeEncabezado(product.getBarCode() + "\n" +product.getDescription())
                .creationDate(LocalDateTime.now())
                .startReport(data.getStartDate())
                .endReport(data.getEndDate())

                .quantityBuy(buyReport.getSumQuantity())
                .totalBuy(buyReport.getSumTotal())

                .salesData(salesReport)
                .totalSales(totalSales)
//                .sumDiscounts()
                .margen(totalSales- buyReport.getSumTotal())
                .build();
        System.out.println("Aqui 10");


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule()); // Para serializar LocalDateTime
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonData;
        //objectMapper.writeValueAsString(generateReportGetDTO);
        System.out.println("Aqui 11");

        try {
            jsonData = objectMapper.writeValueAsString(generateReportGetDTO);
        }catch (Exception e){
            System.out.println("Aqui 12");

            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Error al converitr json a string");
        }

        System.out.println("Aqui 13");

        Business business = businessRepository.findById(idBusiness).orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDates = data.getStartDate().format(formatter) + " - " + data.getEndDate().format(formatter);
        System.out.println("Aqui 14");

        Report report = Report.builder()
                .title(data.getTitle())
                .type(1)
                .creationDate(LocalDateTime.now())
                .fechasRango(formattedDates)
                .script(jsonData)
                .business(business)
                .build();


        System.out.println("Aqui 15");


        Report r = reportRepository.save(report);
        System.out.println("Aqui 16");

        GETREPORT returnR = GETREPORT.builder()
                .idReport(r.getId())
                .type(r.getType())
                .report(generateReportGetDTO)
                .build();
        return returnR;

    }



    public void generateReportByCategory(CreateReportGenerateProductDTO data){

    }



    public List<GetViewReportDTO> getAll(){
        return reportRepository.findAll().stream().map(r->
                new GetViewReportDTO(r.getId(),r.getTitle(),r.getCreationDate(),r.getFechasRango())
        ).collect(Collectors.toList());

    }

}
