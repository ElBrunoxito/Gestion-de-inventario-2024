package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.TypeMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMovementRepository extends JpaRepository<TypeMovement,Integer> {

    TypeMovement findByName(TypeMovement.TYPE name);

}
