package com.example.checkscam.repository;

import com.example.checkscam.entity.UrlScam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlScamRepository extends JpaRepository<UrlScam, Long> {
    UrlScam findByInfo(String info);
}