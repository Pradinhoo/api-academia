package com.api_academia.exception.professor;

public class ProfessorNaoEncontradoException extends RuntimeException {
    public ProfessorNaoEncontradoException(Long idProfessor) {
        super(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, idProfessor));
    }
}
