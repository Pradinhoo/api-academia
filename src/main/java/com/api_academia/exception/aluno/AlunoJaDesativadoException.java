package com.api_academia.exception.aluno;

public class AlunoJaDesativadoException extends RuntimeException {
    public AlunoJaDesativadoException(Long idAluno) {
        super(String.format(MensagensDeErroAluno.ALUNO_JA_DESATIVADO, idAluno));
    }
}
