package com.api_academia.mapper;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.model.AulaPersonal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AulaPersonalMapper {

    @Mapping(target = "id", ignore = true)
    AulaPersonal toEntity(AulaPersonalDTO aulaPersonalDTO);

    AulaPersonalDTO toDto(AulaPersonal aulaPersonal);
}
