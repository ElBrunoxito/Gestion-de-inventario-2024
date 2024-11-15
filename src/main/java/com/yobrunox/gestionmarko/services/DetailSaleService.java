package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.DeletingUUIDPdto;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO;
import com.yobrunox.gestionmarko.dto.sale.DetailSaleAddDTO;
import com.yobrunox.gestionmarko.dto.sale.DetailSaleGetDTO;
import com.yobrunox.gestionmarko.models.DetailBuy;
import com.yobrunox.gestionmarko.models.DetailSale;
import com.yobrunox.gestionmarko.models.Product;
import com.yobrunox.gestionmarko.repository.DetailSaleRepository;
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
public class DetailSaleService {
    private final DetailSaleRepository detailSaleRepository;
    private final ProductRepository productRepository;
    private final MovementsService movementsService;
    private final ProductService productService;
    //private final MovementsRepository movementsRepository;

    public List<DetailSaleGetDTO> getAllDetailSaleForIdSale(UUID idSale){
        return detailSaleRepository.getAllByIdSale(idSale).orElse(null).stream()
                .map(ds->{
                    ProductGetUserDTO product = productRepository.getProductDTOByIdDetailSale(ds.getIdDetailSale());

                    return new DetailSaleGetDTO(
                            ds.getIdDetailSale(),
                            //db.getDescription(),
                            ds.getQuantity(),
                            ds.getPrice(),
                            product
                    );
                })
                .collect(Collectors.toList());



    }

    @Transactional
    public DetailSale addDetailSale(DetailSaleAddDTO detailSaleAddDTO){
        Product product = productRepository.findById(detailSaleAddDTO.getIdProduct()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado")
        );

        /*if(product.getCurrentStock() < detailSaleAddDTO.getQuantity()){
            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST,"El stock de P:" + product.getDescription() + " es menor al stock actual: " + product.getCurrentStock());
        }*/
        //System.out.println("jajaja " + detailSaleAddDTO.getIdProduct());

        DetailSale detailSale = DetailSale.builder()
                .quantity(detailSaleAddDTO.getQuantity())
                .price(detailSaleAddDTO.getPrice())
                //.dueDate(detailBuyAddDTO.getDueDate())
                //.movements()
                .sale(detailSaleAddDTO.getSale())
                .product(product)

                .build();

        DetailSale detailSaleSAVE = detailSaleRepository.saveAndFlush(detailSale);

        //MOVEMENTS EN CREATE
        movementsService.createMovementSale(product,detailSaleSAVE);

        return detailSaleSAVE;
    }


    @Transactional
    public DetailSale updateDetailSale(DetailSaleAddDTO detailSaleAddDTO){
        /*if(!detailBuyRepository.existsById(detailBuyAddDTO.getIdDetailBuy())){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Detalle no encontrado");
        }*/

        Product product = productRepository.findById(detailSaleAddDTO.getIdProduct()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado")
        );

//        System.out.println("AQUI 7");
        DetailSale detailSale = detailSaleRepository.findById(detailSaleAddDTO.getIdDetailSale()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Detalle no encontrado")
        );
        Integer value = detailSale.getQuantity();

                detailSale.setIdDetailSale(detailSaleAddDTO.getIdDetailSale());
                detailSale.setQuantity(detailSaleAddDTO.getQuantity());
                detailSale.setPrice(detailSaleAddDTO.getPrice());
                //.dueDate(detailSaleAddDTO.getDueDate()
                //.movements()
                detailSale.setSale(detailSaleAddDTO.getSale());
                detailSale.setProduct(product);
        System.out.println("AQUI 8");
        DetailSale detailSaleSAVE = detailSaleRepository.saveAndFlush(detailSale);
        //MOVEMENT UPDATE
        movementsService.updateMovementSale(product,detailSaleSAVE,value);

        return detailSaleSAVE;

        //return detailBuyRepository.saveAndFlush(detailBuy);
    }




    @Transactional
    public void deleteDetailSaleIsntExisting(List<UUID> idList, UUID idSale){
        List<UUID> existingIds = detailSaleRepository.findAllIds();
        System.out.println("AQU ptr");

        // a !a = a v
/*        List<UUID> idList = list.stream()
                .map(DeletingUUIDPdto::getId)
                .toList();
*/

        Set<UUID> idsToDelete = existingIds.stream()
                //AQUI
                .filter(id -> !idList.contains(id))
                .collect(Collectors.toSet());
        System.out.println("AQU seix");

        if (!idsToDelete.isEmpty()) {
            System.out.println("krajo");

            //EN MOVIMIENTOS
            movementsService.deleteMovementDetailsSales(idsToDelete);

            //EN DETAILS BUY
            detailSaleRepository.deleteByIds(idsToDelete,idSale);

        }

        System.out.println("FIN");
    }













    }
