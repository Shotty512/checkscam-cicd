package com.example.checkscam.service;

import com.example.checkscam.dto.ScamTypesDto;
import com.example.checkscam.exception.CheckScamException;

import java.util.List;

public interface ScamTypesService
{
    ScamTypesDto createScamType(ScamTypesDto scamTypesDto);

    List<ScamTypesDto> getAllScamTypes();

    ScamTypesDto updateScamType(Long id, ScamTypesDto scamTypesDto) throws CheckScamException;

    boolean deleteScamType(Long id) throws CheckScamException;
}
