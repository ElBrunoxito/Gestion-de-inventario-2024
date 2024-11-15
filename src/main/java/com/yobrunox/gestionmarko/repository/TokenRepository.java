package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
}
