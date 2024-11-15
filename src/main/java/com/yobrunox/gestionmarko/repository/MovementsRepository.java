package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Movements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MovementsRepository extends JpaRepository<Movements, UUID> {


//    @Query("SELECT M.idMovement FROM Movements M " +
//            "WHERE M.product = :idP AND M.detailBuy.idDetailBuy = :idDB")
//    UUID getIdForProductAndIdDAndDetailBuy();

    @Query("SELECT M.idMovement FROM Movements M " +
            "WHERE M.detailBuy.idDetailBuy = :idDB")
    Optional<UUID> getIdForProductAndIdDAndDetailBuy(/*@Param("idP") UUID idProduct,*/
                                           @Param("idDB") UUID idDetailBuy);
//            "WHERE M.product.idProduct = :idP AND M.detailBuy.idDetailBuy = :idDB")

    @Query("SELECT M.idMovement FROM Movements M " +
            "WHERE M.detailSale.idDetailSale = :idDS")
    Optional<UUID> getIdForProductAndIdDAndDetailSale(/*@Param("idP") UUID idProduct,*/
                                                     @Param("idDS") UUID idDetailSale);
//            "WHERE /*M.product.idProduct = :idP AND/*/ M.detailSale.idDetailSale = :idDS")

//    void deleteByProduct_IdProductAndDetailBuy_IdDetailBuy(UUID product_idProduct, UUID detailBuy_idDetailBuy);
    //void deleteAllByDetailBuy_IdDetailBuy(UUID detailBuy_idDetailBuy);

    @Modifying
    @Query("DELETE FROM Movements M WHERE M.detailBuy.idDetailBuy IN :idDetailBuy")
    void deleteByIdsAndDetailBuys(@Param("idDetailBuy") Set<UUID> idDetailBuy);

    @Modifying
    @Query("DELETE FROM Movements M WHERE M.detailSale.idDetailSale IN :idDetailSale")
    void deleteByIdsAndDetailSales(@Param("idDetailSale") Set<UUID> idDetailSale);


    @Query(value = "SELECT m FROM Movements m WHERE m.detailBuy.idDetailBuy IN :idsDetailBuy")
    List<Movements> findByDetailBuyIds(@Param("idsDetailBuy") Set<UUID> idsDetailBuy);

    @Query(value = "SELECT m FROM Movements m WHERE m.detailSale.idDetailSale IN :idsDetailSale")
    List<Movements> findByDetailSaleIds(@Param("idsDetailSale") Set<UUID> idsDetailSale);

    //List<Movements> findAllByDetailBuy_IdDetailBuy(UUID detailBuy_idDetailBuy);
    //List<Movements> findByDetailSale_IdDetailSale(Set<UUID> idDetailSale);


    @Query(value = "SELECT * FROM movements WHERE product_id_product = :idProduct AND type_movement_id_type_movement = 1 AND stock_asignado != 0 ORDER BY update_date ASC LIMIT 1", nativeQuery = true)
    Movements findNextValidMovement(@Param("idProduct") UUID idProduct);

/*
    @Query("SELECT * FROM Movements M WHERE M.typeMovement.name = 'IN' ")
    Movements getLastMovementForBuy();
    */
}
