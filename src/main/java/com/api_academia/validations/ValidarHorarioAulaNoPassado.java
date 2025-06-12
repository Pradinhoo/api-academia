package com.api_academia.validations;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.exception.aulapersonal.HorarioAulaNoPassadoException;

import java.time.LocalDateTime;

public class ValidarHorarioAulaNoPassado implements ValidarCadastroAula {

    public void validar(AulaPersonalDTO dados) {
        if (dados.dataHoraAula().isBefore(LocalDateTime.now())) {
            throw new HorarioAulaNoPassadoException();
        }
    }
}
