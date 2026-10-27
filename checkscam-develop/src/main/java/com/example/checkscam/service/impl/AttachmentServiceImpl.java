package com.example.checkscam.service.impl;

import com.example.checkscam.repository.AttachmentRepository;
import com.example.checkscam.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository repository;


}
