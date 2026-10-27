package com.example.checkscam.service.mapper;

import com.example.checkscam.dto.AttachmentDto;
import com.example.checkscam.entity.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentDto entityToDto(Attachment attachment);
}
