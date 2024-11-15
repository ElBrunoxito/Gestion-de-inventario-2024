package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT COUNT(U) > 0 FROM UserEntity U WHERE U.username = :username")
    Boolean existsByUsername(@Param("username") String username);



    /*@Query(value = "SELECT U FROM UserEntity U WHERE U.username= :username")
    Optional<UserEntity> findByUser (@Param("username") String username);*/
}
