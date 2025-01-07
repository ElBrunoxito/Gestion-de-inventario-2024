package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.DeletingUUIDPdto;
import com.yobrunox.gestionmarko.dto.buy.BuyAddDTO;
import com.yobrunox.gestionmarko.dto.buy.BuyGetDTO;
import com.yobrunox.gestionmarko.dto.buy.BuyWithDetailsGetDTO;
import com.yobrunox.gestionmarko.dto.buy.DetailBuyAddDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.Buy;
import com.yobrunox.gestionmarko.models.TypeComprobante;
import com.yobrunox.gestionmarko.repository.BuyRepository;
import com.yobrunox.gestionmarko.repository.DetailBuyRepository;
import com.yobrunox.gestionmarko.repository.TypeComprobanteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuyService {

    private final BuyRepository buyRepository;
    private final DetailBuyService detailBuyService;

    private final TypeComprobanteRepository typeComprobanteRepository;
    private final DetailBuyRepository detailBuyRepository;

    public List<BuyGetDTO> getAllForBusiness(Long idBusiness){
        return buyRepository.getAllForBusiness(idBusiness).orElse(new ArrayList<>());
    }

    public BuyWithDetailsGetDTO getBuyWithDetails(UUID idBuy){
        if(!buyRepository.existsById(idBuy)){
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"Ha ocurrido un error la encontrar compra");
        }

        Buy buy = buyRepository.findById(idBuy).orElse(null);

        BuyWithDetailsGetDTO buyWithDetails = BuyWithDetailsGetDTO.builder()
                .idBuy(idBuy)
                .nroBuy(buy.getNroBuy())
                .buyUpdateDate(buy.getBuyUpdateDate())
                .buyTotal(buy.getBuyTotal())
                .idTypeComprobante(buy.getTypeComprobante().getIdTypeComprobante())
                .buys(detailBuyService.getAllDetailsBuyForIdBuy(idBuy))
                .build();

        return buyWithDetails;
    }



    @Transactional
    public Buy createBuy(BuyAddDTO buyAddDTO){

        //Calculamos el total price*quiantity
        Double total = buyAddDTO.getDetailsBuy().stream()
                .mapToDouble(detail ->detail.getPrice() * detail.getQuantity())
                .sum();

        //

        boolean isAnywayZero = buyAddDTO.getDetailsBuy().stream()
                .anyMatch(detail -> detail.getQuantity() == 0|| detail.getPrice() == 0);

        if(isAnywayZero){
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No puede ser 0 ");
        }

        TypeComprobante typeComprobante = typeComprobanteRepository.findById(buyAddDTO.getIdTypeComprobante()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No se encontro tipo de comprobante")
        );

        Buy buy = Buy.builder()
                .nroBuy(buyAddDTO.getNroBuy())
                .buyDate(LocalDateTime.now())
                .buyUpdateDate(LocalDateTime.now())
                .buyTotal(total)
                .typeComprobante(typeComprobante)
                .user(buyAddDTO.getUser())

                //.detailBuys(buyAddDTO.getDetailsBuy())
                .build();

        Buy buySave = buyRepository.save(buy);
        /*if(buySave == null){
            throw new BusinessException("M-500",HttpStatus.BAD_REQUEST,"Ha ocurrido un error al guardar compra");
        }*/

        List<DetailBuyAddDTO> details = buyAddDTO.getDetailsBuy();
        for(var d : details){
            d.setBuy(buySave);
            detailBuyService.addDetailBuy(d);
        }
        return buy;
    }


    @Transactional
    public Buy updateBuy(BuyAddDTO buyAddDTO){

        if(!buyRepository.existsById(buyAddDTO.getIdBuy())){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"No se encontro la compra");
        }


        //Calculamos el total price*quiantity
        Double total = buyAddDTO.getDetailsBuy().stream()
                .mapToDouble(detail ->detail.getPrice() * detail.getQuantity())
                .sum();

        //
        boolean isAnywayZero = buyAddDTO.getDetailsBuy().stream()
                .anyMatch(detail -> detail.getQuantity() == 0|| detail.getPrice() == 0);

        if(isAnywayZero){
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No puede ser 0 ");
        }

        TypeComprobante typeComprobante = typeComprobanteRepository.findById(buyAddDTO.getIdTypeComprobante()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No se encontro tipo de comprobante")
        );

        Buy buy = buyRepository.findById(buyAddDTO.getIdBuy()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"No se encontro la compra")
        );

        Buy buyBuild = Buy.builder()
                .idBuy(buy.getIdBuy())
                .nroBuy(buyAddDTO.getNroBuy())
                .buyDate(buy.getBuyDate())
                .buyUpdateDate(LocalDateTime.now())
                .buyTotal(total)
                .typeComprobante(typeComprobante)
                .user(buyAddDTO.getUser())
                //.detailBuys(buyAddDTO.getDetailsBuy())
                .build();

        Buy buySave = buyRepository.save(buyBuild);


        /*if(buySave == null){
            throw new BusinessException("M-500",HttpStatus.BAD_REQUEST,"Ha ocurrido un error al guardar compra");
        }/*/

        List<DetailBuyAddDTO> details = buyAddDTO.getDetailsBuy();

        SHOW(details);
        //Integer totalRes = details.stream()
//                .forEach();

        List<UUID> uuidList = details.stream()
                .map(DetailBuyAddDTO::getIdDetailBuy)
                .collect(Collectors.toList());

        detailBuyService.deleteDetailBuyIsntExisting(uuidList,buySave.getIdBuy());
        ////////////////////
        SHOW(details);



        for(var d: details){


            if (d.getIdDetailBuy() == null) {
                d.setBuy(buySave);
                // Crear nuevo detalle de compra
                detailBuyService.addDetailBuy(d);
            } else {
                // Si el ID no es nulo, se verifica si existe
                if (detailBuyRepository.existsById(d.getIdDetailBuy())) {
                    d.setBuy(buySave);
                    detailBuyService.updateDetailBuy(d);
                    System.out.println("Actualizar");
                } else {
                    // Si no existe, lanza una excepción o maneja el caso como desees
                    throw new BusinessException("M-400", HttpStatus.NOT_FOUND, "Detalle de compra no encontrado");
                }
            }
        }



        return buySave;
    }
    public void deleteBuy(UUID idBuy){




        buyRepository.deleteById(idBuy);
    }




    private void SHOW(List<DetailBuyAddDTO> details){
        for (DetailBuyAddDTO detail : details) {
            System.out.println("Detalle de Compra:");
            System.out.println("ID de Detalle: " + detail.getIdDetailBuy());
            System.out.println("Cantidad: " + detail.getQuantity());
            System.out.println("Precio: " + detail.getPrice());
            System.out.println("Fecha de Vencimiento: " + detail.getDueDate());

            // Si quieres imprimir información de la compra (buy), asegúrate de tener un método toString() en la clase Buy
            if (detail.getBuy() != null) {
                System.out.println("ID de Compra: " + detail.getBuy().getIdBuy()); // Suponiendo que Buy tiene un método getIdBuy()
            }

            System.out.println("ID de Producto: " + detail.getIdProduct());
            System.out.println("---------------------------");
        }
    }
}


