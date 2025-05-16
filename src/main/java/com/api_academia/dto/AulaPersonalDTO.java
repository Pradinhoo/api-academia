package com.api_academia.dto;

import com.api_academia.model.AulaPersonal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AulaPersonalDTO(

        @NotNull
        Long idAluno,

        @NotNull
        Long idProfessor,

        @NotBlank
        LocalDateTime dataHoraAula,

        @NotBlank
        LocalDateTime dataHoraAulaFim) {

        public AulaPersonalDTO(AulaPersonal aula) {
                this(aula.getAluno().getId(), aula.getProfessor().getId(), aula.getDataHoraAula(), aula.getDataHoraAulaFim());
        }
}
