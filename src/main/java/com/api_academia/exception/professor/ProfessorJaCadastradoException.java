package com.api_academia.exception.professor;

public class ProfessorJaCadastradoException extends RuntimeException {
    public ProfessorJaCadastradoException() {
        super(MensagensDeErroProfessor.PROFESSOR_JA_CADASTRADO);
    }
}
