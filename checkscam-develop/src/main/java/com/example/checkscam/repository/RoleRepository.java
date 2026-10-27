package com.example.checkscam.repository;

import com.example.checkscam.constant.RoleName;
import com.example.checkscam.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}