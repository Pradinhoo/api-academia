package com.api_academia.dto;

import jakarta.validation.constraints.NotNull;

public record ProgressoDTO(

        @NotNull (message = "Esse campo é obrigatório")
        Double peso,

        @NotNull (message = "Esse campo é obrigatório")
        Double altura,

        @NotNull (message = "Esse campo é obrigatório")
        Long idAluno) {
}
