package com.yobrunox.gestionmarko.services;

import com.itextpdf.text.DocumentException;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.sale.collect.CollectAddDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.PdfAddDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.ProductsPDFAddDTO;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.models.Collect;
import com.yobrunox.gestionmarko.models.Sale;
import com.yobrunox.gestionmarko.models.TypePayment;
import com.yobrunox.gestionmarko.repository.CollectRepository;
import com.yobrunox.gestionmarko.repository.SaleRepository;
import com.yobrunox.gestionmarko.repository.TypePaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollectService {


    private final SaleRepository saleRepository;
    private final TypePaymentRepository typePaymentRepository;
    private final CollectRepository collectRepository;
    private final PdfService pdfService;
    private final SaleService saleService;
    private final FirebaseStorageService firebaseStorageService;

    public Collect addCollect(CollectAddDTO collectAddDTO, Business business,Sale sale){

        System.out.println("AQUI 1 ");
        TypePayment typePayment = typePaymentRepository.findById(collectAddDTO.getIdTypePayment()).orElseThrow(
                ()->new BusinessException("M-400", HttpStatus.NOT_FOUND, "Tipo de pago no existe")
        );

        Double amountCollection = sale.getSaleTotal()  - collectAddDTO.getDiscountAmount();

        System.out.println("AQUI 2");

        //HttpStatus.PERMANENT_REDIRECT
        //Get data for the business
        if((business.getName() == null || business.getName().isEmpty()) ||
                (business.getRuc() == null || business.getRuc().isEmpty()) ||
                (business.getAddress() == null || business.getAddress().isEmpty()) ||
                (business.getPhone() == null || business.getPhone().isEmpty()) ||
                (business.getUrlImage() == null || business.getUrlImage().isEmpty()) ||
                (business.getDescription() == null || business.getDescription().isEmpty())
        ) {
            throw new BusinessException("M-302", HttpStatus.FOUND, "Debe rellenar todos los datos de negocio");
        }

        System.out.println("AQUI 3");


        List<ProductsPDFAddDTO> list = sale.getDetailSales().stream()
                .map(ds -> {
                    return  new ProductsPDFAddDTO(ds.getQuantity().floatValue(),ds.getProduct().getDescription(), ds.getPrice(),ds.getPrice()* ds.getQuantity());
                    //return new ProductsPDFAddDTO(ds.getQuantity(),ds.ge)

                })
                .collect(Collectors.toList());
        System.out.println("AQUI  34");

        PdfAddDTO pdfAddDTO = PdfAddDTO.builder()
                .urlImage(business.getUrlImage())
                .nameBusiness(business.getName())
                .address(business.getAddress())
                .addressBusiness(business.getAddress())
                .phoneBusiness(business.getPhone())
                .code(sale.getCode())
                .customer("TODOS")
                .address("ADDRESS")
                .dateCollect(new Date())
                //.moneyType("SOL (PEN)")
                .moneyType(typePayment.getName().toUpperCase())
                .discountAmount(collectAddDTO.getDiscountAmount())
                .products(list)
                .total(sale.getSaleTotal())
                .message("jejejejeje GRACIAS")
                .build();
        String pdfURL = "";

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        try {
            pdf = pdfService.generateStyledPdf(pdfAddDTO);
            pdfURL = firebaseStorageService.createPdfBusiness(pdf,business.getId(),sale.getCode());

        } catch (DocumentException | IOException e) {
            throw new BusinessException("M-500", HttpStatus.NOT_FOUND, "Ha ocurrido un error al generar la boleta");

        }



        Collect collect = Collect.builder()
                .creationCollectDate(LocalDateTime.now())
                .discountAmount(collectAddDTO.getDiscountAmount())
                .amountCollection(amountCollection)
                .stateCollection(true)
                .urlPdf(pdfURL)
                //.script(generateInvoice())
                .sale(sale)
                .typePayment(typePayment)
                .build();



        return collectRepository.save(collect);
    }


}
