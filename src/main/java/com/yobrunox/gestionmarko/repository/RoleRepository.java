package com.yobrunox.gestionmarko.repository;

import com.yobrunox.gestionmarko.models.ERole;
import com.yobrunox.gestionmarko.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRole(ERole role);

}
