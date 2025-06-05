package com.api_academia.mapper;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.model.Aluno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    @Mapping(target = "id", ignore = true)
    Aluno toEntity(AlunoDTO alunoDTO);

    AlunoDTO toDto(Aluno aluno);
}
