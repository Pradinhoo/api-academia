package com.api_academia.mapper;

import com.api_academia.dto.EnderecoDTO;
import com.api_academia.model.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    Endereco toEntity(EnderecoDTO enderecoDTO);

    EnderecoDTO toDto(Endereco endereco);
}
