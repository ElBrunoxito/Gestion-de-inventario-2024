package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {


    @Query("SELECT C " +
            "FROM Category C " +
            "WHERE C.business.id = :idB AND  C.name = :nameC")
    Optional<Category> findForNameBusinessById(@Param("nameC")String nameC, @Param("idB")Long idB);


    Optional<Category> findCategoryByIdAndBusiness_Id(Long id,Long businessId);
    Optional<Category> findCategoryByNameAndBusiness_Id(String name, Long businessId);


    @Query("SELECT C.name FROM Category C WHERE C.business.id = :businessId")
    Optional<Set<String>> getAllCategories(@Param("businessId") Long business_id);










    boolean existsByIdAndBusiness_Id(Long id, Long businessId);
    boolean existsByNameAndBusiness_Id(String name, Long id);


}
