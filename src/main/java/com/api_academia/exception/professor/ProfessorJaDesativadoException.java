package com.api_academia.exception.professor;

public class ProfessorJaDesativadoException extends RuntimeException {
    public ProfessorJaDesativadoException() {
        super(MensagensDeErroProfessor.PROFESSOR_JA_DESATIVADO);
    }
}
