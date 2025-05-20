package com.api_academia.dto;


import jakarta.validation.constraints.Pattern;

public record AtualizaEnderecoDTO(

        String logradouro,

        String numero,

        String complemento,

        String cidade,

        String estado,

        @Pattern(regexp = "\\d{8}", message = "O campo CEP deve conter 8 dígitos")
        String cep) {}
