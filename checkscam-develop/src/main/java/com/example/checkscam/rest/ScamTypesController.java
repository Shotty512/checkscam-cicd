package com.example.checkscam.rest;

import com.example.checkscam.dto.ScamTypesDto;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.ScamTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scam-types")
@RequiredArgsConstructor
public class ScamTypesController {

    private final ScamTypesService scamTypesService;

    @GetMapping("/batch")
    public CheckScamResponse<List<ScamTypesDto>> getAllScamTypes() {
        return new CheckScamResponse<>(scamTypesService.getAllScamTypes());
    }

    @PostMapping
    public CheckScamResponse<ScamTypesDto> createScamType(@RequestBody ScamTypesDto scamTypesDto) {
        return new CheckScamResponse<>(scamTypesService.createScamType(scamTypesDto));
    }

    @PutMapping("/{id}")
    public CheckScamResponse<ScamTypesDto> updateScamType(@PathVariable Long id, @RequestBody ScamTypesDto scamTypesDto) throws CheckScamException {
        return new CheckScamResponse<>(scamTypesService.updateScamType(id, scamTypesDto));
    }

    @DeleteMapping("/{id}")
    public CheckScamResponse<Boolean> deleteScamType(@PathVariable Long id) throws CheckScamException {
        return new CheckScamResponse<>(scamTypesService.deleteScamType(id));
    }

}
