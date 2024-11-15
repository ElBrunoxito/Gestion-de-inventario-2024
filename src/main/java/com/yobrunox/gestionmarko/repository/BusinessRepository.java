package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

//public interface BusinessRepository extends ReactiveCrudRepository<Business, Long> {

//    Optional<Business> findByName(String name);
}
/*
@Repository
public interface BusinessReactiveRepository extends ReactiveCrudRepository<Business, Long> {

//public interface BusinessRepository extends ReactiveCrudRepository<Business, Long> {

//    Optional<Business> findByName(String name);
}


*/