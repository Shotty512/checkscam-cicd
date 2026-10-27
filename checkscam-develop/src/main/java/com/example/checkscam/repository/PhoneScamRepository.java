package com.example.checkscam.repository;

import com.example.checkscam.entity.PhoneScam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneScamRepository extends JpaRepository<PhoneScam, Long> {
    PhoneScam findByPhoneNumber(String info);
}