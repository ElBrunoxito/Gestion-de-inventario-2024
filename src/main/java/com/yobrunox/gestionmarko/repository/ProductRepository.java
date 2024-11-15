package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.dto.product.ProductGetAdminDTO;
import com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO;
import com.yobrunox.gestionmarko.dto.product.ProductoGetDTO;
import com.yobrunox.gestionmarko.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {



    @Query("SELECT new com.yobrunox.gestionmarko.dto.product.ProductoGetDTO(P.idProduct,P.barCode,P.description,P.unit.name,P.initialStock,P.currentStock,P.category.name,P.state) " +
            "FROM Product P WHERE P.business.id = :idBusiness ORDER BY P.barCode")
    Optional<List<ProductoGetDTO>> getAllProducts(@Param("idBusiness") Long idBusiness);

//    Optional<List<ProductoGetDTO>> getAllByBusiness_Id(Long idB);
    //Optional<List<ProductoGetDTO>> findAllByBusiness_Id(Long idB);


    @Query("SELECT new com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO(P.idProduct,P.barCode,CONCAT(P.description,' x ',P.unit.name)) " +
            "FROM Product P WHERE P.idProduct = :idProduct ORDER BY P.barCode")
    Optional<ProductGetUserDTO> getProductByIdUser(@Param("idProduct") UUID idProduct);



    @Query("SELECT new com.yobrunox.gestionmarko.dto.product.ProductGetAdminDTO(P.idProduct,P.barCode,P.description,P.initialStock,P.category.name,P.unit.name) " +
            "FROM Product P WHERE P.idProduct = :idProduct ORDER BY P.barCode")
    Optional<ProductGetAdminDTO> getProductByIdAdmin(@Param("idProduct") UUID idProduct);


    Optional<Product> findProductByIdProductAndBusiness_Id(UUID uuid,Long id);

    //Long findByCurrentStockAndIdProductAndAndBusiness_Id(UUID uuid, Long id);
    /*@Query("SELECT P.currentStock FROM Product P WHERE P.idProduct = :idProduct")
    Long getCurrentStockByIdProduct(@Param("idProduct") UUID idProduct);*/



    //Interacion con detauls buys
    @Query("SELECT new com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO(DB.product.idProduct,DB.product.barCode,DB.product.description) " +
            "FROM DetailBuy DB WHERE DB.idDetailBuy = :idDB ")
    ProductGetUserDTO getProductDTOByIdDetailBuy(@Param("idDB") UUID idDB );

    @Query("SELECT new com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO(DS.product.idProduct,DS.product.barCode,DS.product.description) " +
            "FROM DetailSale DS WHERE DS.idDetailSale = :idDS ")
    ProductGetUserDTO getProductDTOByIdDetailSale(@Param("idDS") UUID idDS);

}
