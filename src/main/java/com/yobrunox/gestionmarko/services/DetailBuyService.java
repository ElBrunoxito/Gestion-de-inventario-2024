package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.DeletingUUIDPdto;
import com.yobrunox.gestionmarko.dto.buy.DetailBuyAddDTO;
import com.yobrunox.gestionmarko.dto.buy.DetailBuyGetDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO;
import com.yobrunox.gestionmarko.models.DetailBuy;
import com.yobrunox.gestionmarko.models.Product;
import com.yobrunox.gestionmarko.repository.DetailBuyRepository;
import com.yobrunox.gestionmarko.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DetailBuyService {

    private final DetailBuyRepository detailBuyRepository;
    private final ProductRepository productRepository;
    private final MovementsService movementsService;
    private final ProductService productService;
    //private final MovementsRepository movementsRepository;

    public List<DetailBuyGetDTO> getAllDetailsBuyForIdBuy(UUID idBuy) {

        return detailBuyRepository  .getAlllByIdBuy(idBuy).orElse(null).stream()
                .map(db -> {
                    ProductGetUserDTO product = productRepository.getProductDTOByIdDetailBuy(db.getIdDetailBuy());

                    return new DetailBuyGetDTO(
                            db.getIdDetailBuy(),
                            //db.getDescription(),
                            db.getQuantity(),
                            db.getPrice(),
                            db.getDueDate(),
                            product
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public DetailBuy addDetailBuy(DetailBuyAddDTO detailBuyAddDTO){
        Product product = productRepository.findById(detailBuyAddDTO.getIdProduct()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado")
        );
        //System.out.println("jajaja " + detailBuyAddDTO.getIdProduct());

        DetailBuy detailBuy = DetailBuy.builder()
                .quantity(detailBuyAddDTO.getQuantity())
                .price(detailBuyAddDTO.getPrice())
                .dueDate(detailBuyAddDTO.getDueDate())
                //.stockRestante(detailBuyAddDTO.getQuantity())
                //.movements()
                .buy(detailBuyAddDTO.getBuy())
                .product(product)


                .remainingQuantity(detailBuyAddDTO.getQuantity())

                .build();

        DetailBuy detailBuySAVE = detailBuyRepository.saveAndFlush(detailBuy);
        //productService.updateStock(detailSale.getQuantity()*-1,product.getIdProduct());


        //MOVEMENTS EN CREATE
        movementsService.createMovementBuy(product,detailBuySAVE);






        return detailBuySAVE;
    }

    @Transactional
    public DetailBuy updateDetailBuy(DetailBuyAddDTO detailBuyAddDTO){
        /*if(!detailBuyRepository.existsById(detailBuyAddDTO.getIdDetailBuy())){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Detalle no encontrado");
        }*/

        Product product = productRepository.findById(detailBuyAddDTO.getIdProduct()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado")
        );
        //System.out.println(detailBuyAddDTO.getIdProduct());


        DetailBuy detailBuy = detailBuyRepository.findById(detailBuyAddDTO.getIdDetailBuy()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Detalle no encontrado")
        );
        Integer value = detailBuy.getQuantity();
/*        DetailBuy detailBuy = DetailBuy.builder()
                .idDetailBuy(detailBuyAddDTO.getIdDetailBuy())
                .quantity(detailBuyAddDTO.getQuantity())
                .price(detailBuyAddDTO.getPrice())
                .dueDate(detailBuyAddDTO.getDueDate())

                /****
                 * ************
                 * ************
                 * *********
                 *
                //stockRestante(detailBuyAddDTO.getQuantity())

                //.movements()
                .buy(detailBuyAddDTO.getBuy())
                .product(product)
                .build();*/
        //detailBuy.setIdDetailBuy(detailBuyAddDTO.getIdDetailBuy());
        detailBuy.setQuantity(detailBuyAddDTO.getQuantity());
        detailBuy.setPrice(detailBuyAddDTO.getPrice());
        detailBuy.setDueDate(detailBuyAddDTO.getDueDate());
        detailBuy.setBuy(detailBuyAddDTO.getBuy());
        detailBuy.setProduct(product);

        detailBuy.setRemainingQuantity(detailBuyAddDTO.getQuantity());


        DetailBuy detailBuySAVE = detailBuyRepository.saveAndFlush(detailBuy);

        //MOVEMENT UPDATE
        movementsService.updateMovementBuy(product,detailBuySAVE,value);

        return detailBuySAVE;

        //return detailBuyRepository.saveAndFlush(detailBuy);
    }




    @Transactional
    public void deleteDetailBuyIsntExisting(List<UUID> idList, UUID idBuy){
        List<UUID> existingIds = detailBuyRepository.findAllIds();
        System.out.println("csmrrrrrrrrrrrrrr");
        for (var k :existingIds){
            System.out.println("id: " + k);
        }

        Set<UUID> idsToDelete = existingIds.stream()
                //AQUI
                .filter(id -> !idList.contains(id))
                .collect(Collectors.toSet());

        if (!idsToDelete.isEmpty()) {

            //EN MOVIMIENTOS
            movementsService.deleteMovementDetailsBuys(idsToDelete);


            //EN DETAILS BUY
            detailBuyRepository.deleteByIds(idsToDelete,idBuy);


        }

    }


    //@Transactional
    //public DetailBuy setStockRestante(Integer stock, DetailBuy detailBuy){
        //detailBuy.setStockRestante(stock);
        //return detailBuyRepository.save(detailBuy);
    //}
   /* @Transactional
    public DetailInBuyDTO g
*/
}
