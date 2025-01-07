package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.MarginSaleBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarginSaleBuyRepository extends JpaRepository<MarginSaleBuy,Long> {


}
