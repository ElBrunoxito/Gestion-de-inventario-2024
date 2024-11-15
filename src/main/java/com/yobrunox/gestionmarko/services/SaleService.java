package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.DeletingUUIDPdto;
import com.yobrunox.gestionmarko.dto.buy.BuyWithDetailsGetDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.sale.DetailSaleAddDTO;
import com.yobrunox.gestionmarko.dto.sale.SaleAddDTO;
import com.yobrunox.gestionmarko.dto.sale.SaleGetDTO;
import com.yobrunox.gestionmarko.dto.sale.SaleWithDetailsGetDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.CollectAddDTO;
import com.yobrunox.gestionmarko.models.Buy;
import com.yobrunox.gestionmarko.models.Sale;
import com.yobrunox.gestionmarko.models.TypeComprobante;
import com.yobrunox.gestionmarko.repository.DetailSaleRepository;
import com.yobrunox.gestionmarko.repository.SaleRepository;
import com.yobrunox.gestionmarko.repository.TypeComprobanteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final TypeComprobanteRepository typeComprobanteRepository;
    private final DetailSaleService detailSaleService;
    private final DetailSaleRepository detailSaleRepository;

    public List<SaleGetDTO> getAllForBusinessAdmin(Long idBusiness){
        return saleRepository.getAllForBusinessAdmin(idBusiness).orElse(new ArrayList<>());
    }

    public List<SaleGetDTO> getAllForBusinessUser(Long idBusiness){
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);  // 00:00:00 de hoy
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return saleRepository.getAllForBusinessUser(idBusiness,startOfDay,endOfDay).orElse(new ArrayList<>());
    }

    public SaleWithDetailsGetDTO getSaleWithDetails(UUID idSale){

        System.out.println("AQUI --");

        if(!saleRepository.existsById(idSale)){
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"Ha ocurrido un error la encontrar venta");
        }
        System.out.println("AQUI 4");


        Sale sale = saleRepository.findById(idSale).orElse(null);
        System.out.println("AQUI 6");
/*
        CollectAddDTO collect = CollectAddDTO.builder()
                .idCollection(sale.getCollect().getIdCollection())
                .discountAmount(sale.getCollect().getDiscountAmount())
                .stateCollection(sale.getCollect().getStateCollection())
                .urlPdf(sale.getCollect().getUrlPdf())
                .idSale(idSale)
                .idTypePayment(sale.getCollect().getTypePayment().getIdTypePayment())
                .build();*/
        UUID idCollect = null;
        if(sale.getCollect() != null){
            idCollect = sale.getCollect().getIdCollection();
        }
        SaleWithDetailsGetDTO saleWithDetails = SaleWithDetailsGetDTO.builder()
                .idSale(sale.getIdSale())
                .code(sale.getCode())
                .saleUpdateDate(sale.getSaleUpdateDate())
                .saleTotal(sale.getSaleTotal())
                .idTypeComprobante(sale.getTypeComprobante().getIdTypeComprobante())
                //.idCollection(sale.getCollect().getIdCollection())
                .idCollection(idCollect)
                .saleDetails(detailSaleService.getAllDetailSaleForIdSale(idSale))
                .build();
        System.out.println("AQUI 7");

        return saleWithDetails;
    }



    private String GENERATE_CODE(TypeComprobante typeComprobante){

        String prefix = typeComprobante.getTypeComprobante().toString().substring(0, 1);

        // Encuentra el último código en la base de datos para el tipo de comprobante
        String lastCode = saleRepository.findLastCodeByDocumentType(typeComprobante.getIdTypeComprobante());

        int nextNumber = 1;

        // Verifica si existe un último código y si comienza con el prefijo esperado
        if (lastCode != null && lastCode.startsWith(prefix)) {
            // Extrae la parte numérica del último código
            String numberPart = lastCode.substring(1);  // Elimina el prefijo "B" o "F"
            nextNumber = Integer.parseInt(numberPart) + 1;  // Incrementa el número
        }

        // Formatea el siguiente número con ceros a la izquierda para que tenga un total de 4 dígitos
        String formattedNumber = String.format("%05d", nextNumber);

        // Devuelve el nuevo código concatenando el prefijo y el número formateado
        return prefix + formattedNumber;  // Por ejemplo, "T0010"

    }

    @Transactional
    public Sale createSale(SaleAddDTO saleAddDTO){

        //Calculamos el total price*quiantity
        Double total = saleAddDTO.getDetailsSale().stream()
                .mapToDouble(detail ->detail.getPrice() * detail.getQuantity())
                .sum();

        //
        boolean isAnywayZero = saleAddDTO.getDetailsSale().stream()
                .anyMatch(detail -> detail.getQuantity() == 0|| detail.getPrice() == 0);

        if(isAnywayZero){
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No puede ser 0 ");
        }

        TypeComprobante typeComprobante = typeComprobanteRepository.findById(saleAddDTO.getIdTypeComprobante()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No se encontro tipo de comprobante")
        );

        String code = GENERATE_CODE(typeComprobante);
        Sale sale = Sale.builder()
                .code(code)
                .saleDate(LocalDateTime.now())
                .saleUpdateDate(LocalDateTime.now())
                .saleTotal(total)
                .typeComprobante(typeComprobante)
                .user(saleAddDTO.getUser())

                //.collect()

                //.detailBuys(buyAddDTO.getDetailsBuy())
                .build();

        Sale saleSave = saleRepository.save(sale);
        /*if(buySave == null){
            throw new BusinessException("M-500",HttpStatus.BAD_REQUEST,"Ha ocurrido un error al guardar compra");
        }*/

        List<DetailSaleAddDTO> details = saleAddDTO.getDetailsSale();
        for(var d : details){
            d.setSale(saleSave);
            detailSaleService.addDetailSale(d);
        }

        // Crear COllection o cobro
        //collectService.createCollect(saleSave,discount,);


        return sale;
    }


    @Transactional
    public Sale updateSale(SaleAddDTO saleAddDTO){

        //****
        //Venta no se puede editar o si
        //****

        //****
        //
        //****
        System.out.println("AQUI -5");

        if(!saleRepository.existsById(saleAddDTO.getIdSale())){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"No se encontro la venta");
        }
        System.out.println("AQUI -6");


        //Calculamos el total price*quiantity
        Double total = saleAddDTO.getDetailsSale().stream()
                .mapToDouble(detail ->detail.getPrice() * detail.getQuantity())
                .sum();

        //
        boolean isAnywayZero = saleAddDTO.getDetailsSale().stream()
                .anyMatch(detail -> detail.getQuantity() == 0|| detail.getPrice() == 0);

        if(isAnywayZero){
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No puede ser 0 ");
        }
        System.out.println("AQUI -7");


        TypeComprobante typeComprobante = typeComprobanteRepository.findById(saleAddDTO.getIdTypeComprobante()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No se encontro tipo de comprobante")
        );
        System.out.println("AQUI -8");

        Sale sale = saleRepository.findById(saleAddDTO.getIdSale()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No se encontro la compra")
        );
        System.out.println("AQUI -9");


        Sale saleBuild = Sale.builder()
                .idSale(sale.getIdSale())
                //Codigo generado no se puede modificar
                .code(sale.getCode())
                .saleDate(sale.getSaleDate())
                .saleUpdateDate(LocalDateTime.now())
                .saleTotal(total)
                .typeComprobante(typeComprobante)
                .user(saleAddDTO.getUser())
                //.detailBuys(buyAddDTO.getDetailsBuy())
                .build();

        Sale saleSave = saleRepository.save(saleBuild);


        /*if(buySave == null){
            throw new BusinessException("M-500",HttpStatus.BAD_REQUEST,"Ha ocurrido un error al guardar compra");
        }/*/

        List<DetailSaleAddDTO> details = saleAddDTO.getDetailsSale();



        List<UUID> uuidList = details.stream()
                .map(DetailSaleAddDTO::getIdDetailSale)
                .collect(Collectors.toList());



        detailSaleService.deleteDetailSaleIsntExisting(uuidList,saleSave.getIdSale());

        for(var d: details){
            if (d.getIdDetailSale() == null) {

                d.setSale(saleSave);
                //System.ou/t.println("id detail: " + d.getIdDetailBuy());
                //System.out.println("product: " + d.getIdProduct());

                // Crear nuevo detalle de compra
                detailSaleService.addDetailSale(d);
            } else {
                System.out.println("AQUI 3");

                // Si el ID no es nulo, se verifica si existe
                if (detailSaleRepository.existsById(d.getIdDetailSale())) {
                    System.out.println("AQUI 4");

                    d.setSale(saleSave);
                    detailSaleService.updateDetailSale(d);

                } else {
                    // Si no existe, lanza una excepción o maneja el caso como desees
                    throw new BusinessException("M-400", HttpStatus.NOT_FOUND, "Detalle de compra no encontrado");
                }
            }
        }



        return saleSave;
    }

    public void deleteSale(UUID idSale){
        saleRepository.deleteById(idSale);
    }

    public boolean saleHasCollect(UUID idSale){
        Sale sale = saleRepository.findById(idSale).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND, "Venta no encontrada")
        );

        return sale.getCollect()!=null?true:false;

    }







}
