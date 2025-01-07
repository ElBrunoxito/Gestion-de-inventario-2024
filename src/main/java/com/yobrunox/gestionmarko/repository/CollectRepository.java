package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Collect;
import com.yobrunox.gestionmarko.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectRepository extends JpaRepository<Collect, UUID> {

    //void deleteBySaleIdSale(UUID idSale);


    boolean existsBySaleIdSale(UUID idSale);
}
