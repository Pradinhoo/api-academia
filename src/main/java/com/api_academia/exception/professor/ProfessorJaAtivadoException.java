package com.api_academia.exception.professor;

public class ProfessorJaAtivadoException extends RuntimeException {
    public ProfessorJaAtivadoException() {
        super(MensagensDeErroProfessor.PROFESSOR_JA_ATIVADO);
    }
}
