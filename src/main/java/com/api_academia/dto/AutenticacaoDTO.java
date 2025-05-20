package com.api_academia.dto;

import jakarta.validation.constraints.NotBlank;

public record AutenticacaoDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        String login,

        @NotBlank (message = "Esse campo é obrigatório")
        String senha) {
}
