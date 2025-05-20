package com.api_academia.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FrequenciaAlunoDTO(

        @NotNull
        LocalDateTime dataHoraCheckIn) {
}
