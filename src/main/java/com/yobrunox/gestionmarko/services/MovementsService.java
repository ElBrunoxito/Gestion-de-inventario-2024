package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.*;
import com.yobrunox.gestionmarko.repository.MovementsRepository;
import com.yobrunox.gestionmarko.repository.TypeMovementRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovementsService {


    private final TypeMovementRepository typeMovementRepository;
    private final MovementsRepository movementsRepository;
    private final ProductService productService;

    public Movements createMovementBuy(Product product, DetailBuy detailBuy){

        TypeMovement typeMovement = typeMovementRepository.findByName(TypeMovement.TYPE.IN);

        productService.updateStock(detailBuy.getQuantity(),product.getIdProduct());

        Movements movements = Movements.builder()
               // .stockAsignado(detailBuy.getQuantity())
                .typeMovement(typeMovement)
                //.creationDate(LocalDateTime.now())
                //.updateDate(LocalDateTime.now())
                .product(product)
                .detailBuy(detailBuy)
                //.detailSale()
                .build();
        return movementsRepository.save(movements);




    }

    public Movements updateMovementBuy(Product product, DetailBuy detailBuy, Integer previousValue){
        /*if(!movementRepository.existsById(idMovement)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Type movement no encontrado");
        }*/
        UUID idMovement = movementsRepository.getIdForProductAndIdDAndDetailBuy(/*product.getIdProduct(),*/detailBuy.getIdDetailBuy()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado")
        );

        if(!movementsRepository.existsById(idMovement)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado");
        }

        Movements m = movementsRepository.findById(idMovement).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado")
        );

        TypeMovement typeMovement = typeMovementRepository.findByName(TypeMovement.TYPE.IN);

        // 40,10 ,  5 ,35
        // 40 ,10 , 5  , -5 35
        //Integer newS = m.getDetailBuy().getQuantity()- previousValue;
        productService.updateBuyUStock(previousValue,m.getDetailBuy().getQuantity(),product.getIdProduct());

        Movements movements = Movements.builder()
                //.detailSale()


                .idMovement(idMovement)
                //.stockSobranteEnCompra(detailBuy.getQuantity())
                //.margen()
                .typeMovement(typeMovement)
                //.creationDate(m.getCreationDate())
                //.updateDate(LocalDateTime.now())
                .product(product)
                .detailBuy(detailBuy)
                //.detailSale()
                .build();
        return movementsRepository.save(movements);
    }

    public void deleteMovementDetailsBuys(Set<UUID> idsDetails){
        //movementsRepository.deleteByProduct_IdProductAndDetailBuy_IdDetailBuy(product.getIdProduct(),detailBuy.getIdDetailBuy());
        //productService.updateStock(totalRes*-1,idProduct);

        //movementsRepository.deleteA;

        //id de detial sale no es de movements
        List<Movements> movementsList = movementsRepository.findByDetailBuyIds(idsDetails);

        for (Movements movement : movementsList) {
            // Obtén el DetailBuy relacionado
            DetailBuy detailBuy = movement.getDetailBuy();
            if (detailBuy != null) {
                // Lógica para calcular el nuevo valor del stock (ajusta según tu lógica de negocio)
                Integer newStockValue = detailBuy.getQuantity()*-1;
                productService.updateStock(newStockValue, detailBuy.getProduct().getIdProduct());
                movementsRepository.delete(movement);
            }

            // Borra el movimiento después de manejar la lógica del stock
        }

        movementsRepository.deleteByIdsAndDetailBuys(idsDetails);
    }



    public void deleteMovementDetailsSales(Set<UUID> idsDetails){
        //movementsRepository.deleteByProduct_IdProductAndDetailBuy_IdDetailBuy(product.getIdProduct(),detailBuy.getIdDetailBuy());
        //productService.updateStock(totalRes,idProduct);


        List<Movements> movementsList = movementsRepository.findByDetailSaleIds(idsDetails);

        for (Movements movement : movementsList) {
            // Obtén el DetailBuy relacionado
            DetailSale detailSale = movement.getDetailSale();
            if (detailSale != null) {
                // Lógica para calcular el nuevo valor del stock (ajusta según tu lógica de negocio)
                Integer newStockValue = detailSale.getQuantity();
                productService.updateStock(newStockValue, detailSale.getProduct().getIdProduct());
                //movementsRepository.deleteById(movement.getIdMovement());
            }

            // Borra el movimiento después de manejar la lógica del stock
        }
        movementsRepository.deleteByIdsAndDetailSales(idsDetails);

    }

    
    /*private saveMovementForCurrentStock(Integer stock){
        Movements
        
    }*/





    public Movements createMovementSale(Product product, DetailSale detailSale){

        TypeMovement typeMovement = typeMovementRepository.findByName(TypeMovement.TYPE.OUT);
        productService.updateStock(detailSale.getQuantity()*-1,product.getIdProduct());

        Movements movements = Movements.builder()
                // .stockAsignado(detailBuy.getQuantity())
                .typeMovement(typeMovement)
                //.creationDate(LocalDateTime.now())
                //.updateDate(LocalDateTime.now())
                .product(product)
                //.detailBuy(detailBuy)
                .detailSale(detailSale)
                .build();

        return movementsRepository.save(movements);

        //boolean completeActualciadoStock = true;
        //Integer sobrante = detailSale.getQuantity();



        //List<Movements> movementsArray;<
        /*Movements lastMovementBuy = movementsRepository.findNextValidMovement(product.getIdProduct());
        while (completeActualciadoStock){






            // 10 < 9
            // 10 < 11 primero prueba
            // 10 < 10
            if(lastMovementBuy.getStockAsignado() < sobrante){
                //10  11
                // = 11 - 10 = 1
                sobrante = sobrante - lastMovementBuy.getStockAsignado();


                lastMovementBuy.getDetailBuy().setStockRestante(sobrante);
                Movements movements = Movements.builder()
                        .stockAsignado(lastMovementBuy.getStockAsignado())
                        .typeMovement(typeMovement)
                        .creationDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .product(product)
                        .detailBuy(lastMovementBuy.getDetailBuy())
                        .detailSale(detailSale)
                        .build();

                movementsRepository.save(movements);
                lastMovementBuy = movementsRepository.findNextValidMovement(product.getIdProduct());
                //completeActualciadoStock = false;
            } else if (lastMovementBuy.getStockAsignado() > sobrante) {
                // 10  9
                // 10  1

                //sobrante = lastMovement.getStockSobranteEnCompra() - sobrante;


                sobrante = sobrante - lastMovementBuy.getStockAsignado();


                lastMovementBuy.getDetailBuy().setStockRestante(sobrante);
                Movements movements = Movements.builder()
                        .stockAsignado(lastMovementBuy.getStockAsignado())
                        .typeMovement(typeMovement)
                        .creationDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .product(product)
                        .detailBuy(lastMovementBuy.getDetailBuy())
                        .detailSale(detailSale)
                        .build();

                movementsRepository.save(movements);
                lastMovementBuy = movementsRepository.findNextValidMovement(product.getIdProduct());


                //lastMovementBuy.setStockAsignado(lastMovementBuy.getStockAsignado() - sobrante);
                //movementsRepository.save(lastMovementBuy);
                completeActualciadoStock = false;
            }else{
                //sobrante = sobrante - lastMovement.getStockSobranteEnCompra();
                lastMovementBuy.setStockAsignado(0);
                movementsRepository.save(lastMovementBuy);
                completeActualciadoStock = false;

            }
            //count++;
        }
*/



        //return movementsRepository.save(movements);
        //return  movementsRepository.sa/

    }





    public Movements updateMovementSale(Product product, DetailSale detailSale,Integer previousValue){
        /*if(!movementRepository.existsById(idMovement)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Type movement no encontrado");
        }*/
        UUID idMovement = movementsRepository.getIdForProductAndIdDAndDetailSale(/*product.getIdProduct(),*/detailSale.getIdDetailSale()).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado")
        );
        /*if(!movementsRepository.existsById(idMovement)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado");
        }*/
        if(!movementsRepository.existsById(idMovement)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado");
        }

        Movements m = movementsRepository.findById(idMovement).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Movement no encontrado")
        );


        TypeMovement typeMovement = typeMovementRepository.findByName(TypeMovement.TYPE.OUT);
        //System.out.println("EN  TYPE");
        //30, , 35, +5
        // 40, -10, 5, -5 35
        //5-10=-5
        //Integer newS = m.getDetailSale().getQuantity()- previousValue;
        productService.updateSaleUStock(previousValue,m.getDetailSale().getQuantity(),product.getIdProduct());

        Movements movements = Movements.builder()
                .idMovement(idMovement)
                .typeMovement(typeMovement)
                .product(product)
                .detailSale(detailSale)
                //.detailSale()
                .build();

        return movementsRepository.save(movements);
    }
}
