package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

    //@Query("SELECT U FROM Unit U WHERE U.name =: name")
    //Optional<Unit> findByName(@Param("name")0 String name);

    Optional<Unit> findByName(String name);

    @Query("SELECT U.name FROM Unit U")
    Optional<Set<String>> getAllUnits();



    boolean existsByName(String name);


}


