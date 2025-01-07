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


    private final TypePaymentRepository typePaymentRepository;
    private final CollectRepository collectRepository;
    private final PdfService pdfService;
    private final FirebaseStorageService firebaseStorageService;
    private final SaleRepository saleRepository;



    /*private void generateCollectPDF(UUID idSale, Business business){
        Sale sale = saleRepository.findById(idSale).orElseThrow(
                ()->new BusinessException("M-500", HttpStatus.BAD_REQUEST, "No se encontro venta")
        );

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
    }
    */

    public Collect addCollect(CollectAddDTO collectAddDTO, Business business,Sale sale){

        TypePayment typePayment = typePaymentRepository.findById(collectAddDTO.getIdTypePayment()).orElseThrow(
                ()->new BusinessException("M-400", HttpStatus.NOT_FOUND, "Tipo de pago no existe")
        );

        Double amountCollection = sale.getSaleTotal()  - collectAddDTO.getDiscountAmount();

        //HttpStatus.PERMANENT_REDIRECT
        //Get data for the business

        Collect collect = Collect.builder()
                .creationCollectDate(LocalDateTime.now())
                .discountAmount(collectAddDTO.getDiscountAmount())
                .amountCollection(amountCollection)
                .stateCollection(true)
                //.urlPdf(pdfURL)
                .sale(sale)
                .typePayment(typePayment)
                .build();

        return collectRepository.save(collect);
    }



    public String generatePdfURL(UUID idCollect,Business business){
        Collect collect = collectRepository.findById(idCollect)
                .orElseThrow(() -> new RuntimeException("Collect not found"));

        TypePayment typePayment = collect.getTypePayment();


        if((business.getName() == null || business.getName().isEmpty()) ||
                (business.getRuc() == null || business.getRuc().isEmpty()) ||
                (business.getAddress() == null || business.getAddress().isEmpty()) ||
                (business.getPhone() == null || business.getPhone().isEmpty()) ||
                (business.getUrlImage() == null || business.getUrlImage().isEmpty()) ||
                (business.getDescription() == null || business.getDescription().isEmpty())
        ) {
            throw new BusinessException("M-302", HttpStatus.FOUND, "Debe rellenar todos los datos de negocio");
        }

        Sale sale = collect.getSale();
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
                .discountAmount(collect.getDiscountAmount())
                .products(list)
                .total(sale.getSaleTotal())
                .message(business.getMessage())
                .build();
        String pdfURL = "";

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        try {
            pdf = pdfService.generateStyledPdf(pdfAddDTO);
            pdfURL = firebaseStorageService.createPdfBusiness(pdf,business.getId(),sale.getCode());

        } catch (DocumentException | IOException e) {
            throw new BusinessException("M-500", HttpStatus.NOT_FOUND, "Ha ocurrido un error al generar la boleta");

        }

        collect.setUrlPdf(pdfURL);
        collectRepository.save(collect);

        return pdfURL;
    }

    public void deleteSale(UUID idSale,Long idBusiness){
        if(collectRepository.existsBySaleIdSale(idSale)){
            Collect collect = saleRepository.findById(idSale).orElse(null).getCollect();
            boolean res = firebaseStorageService.deletePdfBusiness(idBusiness,collect.getUrlPdf());
            System.out.println(res);
        }
        //collectRepository.deleteBySaleIdSale(idSale);
    }

}
