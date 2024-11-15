package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.dto.buy.BuyGetDTO;
import com.yobrunox.gestionmarko.models.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuyRepository extends JpaRepository<Buy,UUID> {

//    @Query("SELECT new com.yobrunox.gestionmarko.dto.buy.BuyGetDTO(B.idBuy,B.nroBuy,B.buyUpdateDate, B.typeComprobante.typeComprobante,b.buyTotal) " +
//            "FROM Buy B WHERE B.user.business.id = :idBusiness")
//    Optional<List<BuyGetDTO>> getAllForBusiness(@Param("idBusiness") Long idBusiness);

//    @Query("SELECT new com.yobrunox.gestionmarko.dto.buy.BuyGetDTO(B.idBuy, B.nroBuy, B.buyUpdateDate, B.typeComprobante.name(), B.buyTotal) " +
//            "FROM Buy B JOIN B.typeComprobante T WHERE B.user.business.id = :idBusiness")
//    Optional<List<BuyGetDTO>> getAllForBusiness(@Param("idBusiness") Long idBusiness);

    @Query("SELECT new com.yobrunox.gestionmarko.dto.buy.BuyGetDTO(B.idBuy, B.nroBuy, B.buyUpdateDate, B.typeComprobante.typeComprobante, SUM(B.buyTotal)) " +
            "FROM Buy B " +
            "WHERE B.user.business.id = :idBusiness " +
            "GROUP BY B.idBuy, B.nroBuy, B.buyUpdateDate, B.typeComprobante ORDER BY B.buyUpdateDate DESC")
    Optional<List<BuyGetDTO>> getAllForBusiness(@Param("idBusiness") Long idBusiness);

    //Buy findByIdBuy(UUID idBuy);
}
