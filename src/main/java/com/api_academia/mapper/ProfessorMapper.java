package com.api_academia.mapper;

import com.api_academia.dto.ProfessorDTO;
import com.api_academia.model.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class})
public interface ProfessorMapper {

    @Mapping(target = "id", ignore = true)
    Professor toEntity(ProfessorDTO professorDTO);

    ProfessorDTO toDto(Professor professor);
}
