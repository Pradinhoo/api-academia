package com.api_academia.exception.aulapersonal;

public class HorarioAulaNoPassadoException extends RuntimeException {
    public HorarioAulaNoPassadoException() {
      super(String.format(MensagensDeErroAulaPersonal.ERRO_AO_VALIDAR_AULA_NO_PASSADO));
    }
}
