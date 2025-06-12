package com.api_academia.exception.aulapersonal;

public class ConflitoAlunoException extends RuntimeException {
    public ConflitoAlunoException() {
      super(String.format(MensagensDeErroAulaPersonal.ERRO_CONFLITO_ALUNO));
    }
}
