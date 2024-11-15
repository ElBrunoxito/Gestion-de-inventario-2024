package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.dto.buy.DetailBuyGetDTO;
import com.yobrunox.gestionmarko.models.DetailBuy;
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
public interface DetailBuyRepository extends JpaRepository<DetailBuy, UUID> {


    @Query("SELECT D.idDetailBuy FROM DetailBuy D")
    List<UUID> findAllIds();

    //@Query("SELECT new com.yobrunox.gestionmarko.dto.buy.DetailBuyGetDTO(D.idDetailBuy/*, D.product.description/*/, D.quantity, D.price, D.dueDate)"+
    @Query("SELECT new com.yobrunox.gestionmarko.dto.buy.DetailBuyGetDTO(D.idDetailBuy, D.quantity, D.price, D.dueDate)"+

            "FROM DetailBuy D WHERE D.buy.idBuy = :idBuy")
    Optional<List<DetailBuyGetDTO>> getAlllByIdBuy(@Param("idBuy") UUID idBuy);



    //void deleteAllByIdDetailBuy(Set<UUID> uuidList);
    @Modifying
    @Query("DELETE FROM DetailBuy D WHERE D.idDetailBuy IN :ids AND D.buy.idBuy = :idBuy")
    void deleteByIds(@Param("ids") Set<UUID> ids, @Param("idBuy") UUID idBuy);
}
