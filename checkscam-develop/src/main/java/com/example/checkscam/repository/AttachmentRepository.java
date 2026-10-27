package com.example.checkscam.repository;

import com.example.checkscam.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    int countByReportId(Long reportId);
}