package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.dto.sale.DetailSaleGetDTO;
import com.yobrunox.gestionmarko.models.DetailSale;
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
public interface DetailSaleRepository extends JpaRepository<DetailSale, UUID> {


    @Query("SELECT D.idDetailSale FROM DetailSale D")
    List<UUID> findAllIds();

    @Query("SELECT new com.yobrunox.gestionmarko.dto.sale.DetailSaleGetDTO(D.idDetailSale,D.quantity,D.price)" +
            "FROM DetailSale D WHERE D.sale.idSale = :idSale")
    Optional<List<DetailSaleGetDTO>> getAllByIdSale(@Param("idSale") UUID idSale);


    //void deleteAllByIdDetailBuy(Set<UUID> uuidList);
    @Modifying
    @Query("DELETE FROM DetailSale D WHERE D.idDetailSale IN :ids AND D.sale.idSale = :idSale")
    void deleteByIds(@Param("ids") Set<UUID> ids, @Param("idSale") UUID idSale);




}
