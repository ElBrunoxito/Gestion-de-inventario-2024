package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.TypePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePaymentRepository extends JpaRepository<TypePayment,Integer> {
}
