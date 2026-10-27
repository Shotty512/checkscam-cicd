package com.example.checkscam.service.impl;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.dto.ScamTypesDto;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.ScamTypesService;
import com.example.checkscam.service.mapper.ScamTypesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScamTypesServiceImpl implements ScamTypesService {
    private final ScamTypesRepository repository;
    private final ScamTypesMapper mapper;

    @Override
    public ScamTypesDto createScamType(ScamTypesDto scamTypesDto) {
        return mapper.entityToResponse(repository.save(mapper.requestToEntity(scamTypesDto)));
    }

    @Override
    public List<ScamTypesDto> getAllScamTypes() {
        return repository.findAll().stream()
                .map(mapper::entityToResponse)
                .toList();
    }

    @Override
    public ScamTypesDto updateScamType(Long id, ScamTypesDto scamTypesDto) throws CheckScamException {
        return mapper.entityToResponse(repository.findById(id)
                .map(entity -> {
                    mapper.setValue(scamTypesDto, entity);
                    return repository.save(entity);
                })
                .orElseThrow(() -> new CheckScamException(ErrorCodeEnum.NOT_FOUND)));
    }

    @Override
    public boolean deleteScamType(Long id) throws CheckScamException {
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return true;
                })
                .orElseThrow(() -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
    }
}
