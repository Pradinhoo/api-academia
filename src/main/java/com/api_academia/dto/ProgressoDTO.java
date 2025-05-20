package com.api_academia.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProgressoDTO(

        @NotNull (message = "Esse campo é obrigatório")
        Double peso,

        @NotNull (message = "Esse campo é obrigatório")
        Double altura,

        @NotNull (message = "Esse campo é obrigatório")
        Long idAluno,

        @NotNull
        LocalDate dataHoraRegistro) {
}
