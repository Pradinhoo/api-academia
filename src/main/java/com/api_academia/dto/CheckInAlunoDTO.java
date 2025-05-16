package com.api_academia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CheckInAlunoDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "Este campo deve conter exatamente 11 dígitos")
        String cpf) {
}
