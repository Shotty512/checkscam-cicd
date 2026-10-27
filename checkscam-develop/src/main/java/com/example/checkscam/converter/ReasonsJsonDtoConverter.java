package com.example.checkscam.converter;

import com.example.checkscam.dto.ReasonsJsonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ReasonsJsonDtoConverter implements AttributeConverter<ReasonsJsonDto, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ReasonsJsonDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi chuyển đối tượng thành JSON", e);
        }
    }

    @Override
    public ReasonsJsonDto convertToEntityAttribute(String json) {
        try {
            return mapper.readValue(json, ReasonsJsonDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi chuyển JSON thành đối tượng", e);
        }
    }
}
