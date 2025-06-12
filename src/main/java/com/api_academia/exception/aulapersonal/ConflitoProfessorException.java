package com.api_academia.exception.aulapersonal;

public class ConflitoProfessorException extends RuntimeException {
    public ConflitoProfessorException() {
        super(String.format(MensagensDeErroAulaPersonal.ERRO_CONFLITO_PROFESSOR));
    }
}
