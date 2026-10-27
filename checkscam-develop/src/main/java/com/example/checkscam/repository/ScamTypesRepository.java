package com.example.checkscam.repository;

import com.example.checkscam.entity.ScamTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScamTypesRepository extends JpaRepository<ScamTypes, Long> {
}