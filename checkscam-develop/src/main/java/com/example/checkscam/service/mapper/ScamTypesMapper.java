package com.example.checkscam.service.mapper;

import com.example.checkscam.dto.ScamTypesDto;
import com.example.checkscam.entity.ScamTypes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScamTypesMapper {

    @Mapping(target = "id", source = "id", conditionExpression = "java(request.getId() != null)")
    ScamTypes requestToEntity(ScamTypesDto request);

    @Mapping(source = "id", target = "id", ignore = true)
    void setValue(ScamTypesDto dto,@MappingTarget ScamTypes entity);

    ScamTypesDto entityToResponse(ScamTypes entity);
}
