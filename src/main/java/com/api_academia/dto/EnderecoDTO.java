package com.api_academia.dto;

import com.api_academia.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        String logradouro,

        @NotBlank (message = "Esse campo é obrigatório")
        String numero,

        String complemento,

        @NotBlank (message = "Esse campo é obrigatório")
        String cidade,

        @NotBlank (message = "Esse campo é obrigatório")
        String estado,

        @NotBlank (message = "Esse campo é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "O campo CEP deve conter 8 dígitos")
        String cep) {

        public EnderecoDTO(Endereco dados) {
                this(dados.getLogradouro(), dados.getNumero(), dados.getComplemento(), dados.getCidade(), dados.getEstado(), dados.getCep());
        }
}
