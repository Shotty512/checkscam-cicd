package com.example.checkscam.repository;

import com.example.checkscam.entity.BankScam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankScamRepository extends JpaRepository<BankScam, Long> {
    BankScam findByBankAccount(String bankAccount);

    BankScam findByBankAccountAndNameAccountAndBankName(String bankAccount, String nameAccount, String bankName);
}