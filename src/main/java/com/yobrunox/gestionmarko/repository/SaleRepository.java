package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.dto.buy.BuyGetDTO;
import com.yobrunox.gestionmarko.dto.dashboard.DateWithPriceDTO;
import com.yobrunox.gestionmarko.dto.sale.SaleGetDTO;
import com.yobrunox.gestionmarko.models.Sale;
import com.yobrunox.gestionmarko.models.TypeComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {

    @Query("SELECT new com.yobrunox.gestionmarko.dto.sale.SaleGetDTO(S.idSale, S.code, S.saleUpdateDate,S.saleTotal,C.discountAmount,C.amountCollection,C.stateCollection,TP.name,S.user.username,C.urlPdf)" +
            "FROM Sale S " +
            "LEFT JOIN Collect C ON C.sale.idSale = S.idSale LEFT JOIN TypePayment TP ON TP.idTypePayment = C.typePayment.idTypePayment WHERE S.user.business.id = :idBusiness ORDER BY S.saleUpdateDate DESC")
            //"GROUP BY S.idSale, S.code, S.saleUpdateDate, S.saleTotal, S.collect.discountAmount,S.collect.amountCollection, S.collect.stateCollection, S.collect.typePayment.name, S.user.username")
    Optional<List<SaleGetDTO>> getAllForBusinessAdmin(@Param("idBusiness") Long idBusiness);


    @Query("SELECT new com.yobrunox.gestionmarko.dto.sale.SaleGetDTO(S.idSale, S.code, S.saleUpdateDate,S.saleTotal,C.discountAmount,C.amountCollection,C.stateCollection,TP.name,S.user.username,C.urlPdf)" +
            "FROM Sale S " +
            "LEFT JOIN Collect C ON C.sale.idSale = S.idSale LEFT JOIN TypePayment TP ON TP.idTypePayment = C.typePayment.idTypePayment WHERE S.user.business.id = :idBusiness AND S.saleDate BETWEEN :start AND :end ORDER BY S.saleUpdateDate DESC ")
    Optional<List<SaleGetDTO>> getAllForBusinessUser(@Param("idBusiness") Long idBusiness, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);






    @Query("SELECT MAX(S.code) FROM Sale S WHERE S.typeComprobante.idTypeComprobante = :idTypeC ORDER BY S.code DESC")
    String findLastCodeByDocumentType(@Param("idTypeC") Integer idTypeC);




    @Query("SELECT  COALESCE(SUM(S.saleTotal),0.0) FROM Collect C " +
            "LEFT JOIN Sale S ON S.idSale = C.sale.idSale " +
            "WHERE S.saleUpdateDate BETWEEN :start AND :end AND C.typePayment.idTypePayment = :idTypePayment")
    Double getMoneySaleINDATES(@Param("start")LocalDateTime start, @Param("end")LocalDateTime end, @Param("idTypePayment") Integer idTypePayment);



    //Dashboard

    @Query("SELECT NEW com.yobrunox.gestionmarko.dto.dashboard.DateWithPriceDTO(SUM(S.saleTotal),DATE(S.saleUpdateDate))" +
            "FROM Sale S GROUP BY DATE(S.saleUpdateDate) ORDER BY DATE(S.saleUpdateDate) ASC")
    Optional<List<DateWithPriceDTO>> getDashBoardForSales();
}
