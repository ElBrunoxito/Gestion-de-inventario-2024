package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.dto.report.GetBuyDataForReport;
import com.yobrunox.gestionmarko.dto.report.GetSalesDataForReportDTO;
import com.yobrunox.gestionmarko.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
/*    @Query("SELECT NEW com.yobrunox.gestionmarko.dto.report.GetBuyDataForReport(" +
            "SUM(DB.quantity), SUM(DB.quantity * DB.price)) " +
            "FROM Buy B " +
            "LEFT JOIN DetailBuy DB ON DB.buy.idBuy = B.idBuy " +
            "INNER JOIN UserEntity U ON U.id = B.user.id " +
            "WHERE B.buyUpdateDate BETWEEN :start AND :end " +
            "AND U.business.id = :idBusiness AND DB.product.idProduct = :idProduct")
*/
    @Query(nativeQuery = true,value = "SELECT IFNULL(SUM(DB.quantity),0), IFNULL(SUM(DB.quantity* DB.price),0)" +
            "FROM buy B" +
            "LEFT JOIN detail_buy DB ON B.id_buy = DB.buy_id_buy" +
            "INNER JOIN user_entity U ON U.id = B.user_id_user" +
            "WHERE B.buy_update_date BETWEEN :start AND :end " +
            "AND U.business_id_business = :idBusiness AND DB.product_id_product = :idProduct")
    Optional<GetBuyDataForReport> getBuysReportForProduct(@Param("idBusiness") Long idBusiness,
                                                          @Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end,
                                                          @Param("idProduct") UUID idProduct);

    @Query(nativeQuery = true, value ="SELECT NEW com.yobrunox.gestionmarko.dto.report.GetSalesDataForReportDTO( U.username,IFNULL(SUM(DS.quantity),0) ,IFNULL(SUM(DS.price),0))" +
            "FROM sale S" +
            "INNER JOIN detail_sale DS ON DS.sale_id_sale = S.id_sale" +
            "INNER JOIN user_entity U ON U.id = S.user_id_user" +
            "LEFT JOIN collect C ON C.sale_id_sale = S.id_sale" +
            "WHERE S.sale_update_date BETWEEN :start AND :end" +
            "AND U.business_id_business = :idBusiness" +
            "AND DS.product_id_product = :idProduct" +
            "AND (CASE " +
            "WHEN :type = 1 THEN C.id_collection IS NOT NULL " +
            "WHEN :type = 2 THEN C.id_collection IS NULL" +
            "WHEN :type = 3 THEN C.id_collection IS NOT NULL OR C.id_collection IS NULL" +
            "END)" +
            "GROUP BY U.id")
    Optional<List<GetSalesDataForReportDTO>> getSalesReportForProduct(
            @Param("idBusiness") Long idBusiness,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("idProduct") UUID idProduct,
            @Param("type") Integer type);

}
