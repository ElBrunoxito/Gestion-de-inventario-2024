package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectRepository extends JpaRepository<Collect, UUID> {
}
