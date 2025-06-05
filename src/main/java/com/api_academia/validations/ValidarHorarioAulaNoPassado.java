package com.api_academia.validations;

import com.api_academia.dto.AulaPersonalDTO;

import java.time.LocalDateTime;

public class ValidarHorarioAulaNoPassado implements ValidarCadastroAula {

    public void validar(AulaPersonalDTO dados) {
        if (dados.dataHoraAula().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível marcar uma aula no passado");
        }
    }
}
