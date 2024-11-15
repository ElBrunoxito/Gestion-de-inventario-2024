package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.TypeComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeComprobanteRepository extends JpaRepository<TypeComprobante,Integer> {
}
