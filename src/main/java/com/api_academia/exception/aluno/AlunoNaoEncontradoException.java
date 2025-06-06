package com.api_academia.exception.aluno;

public class AlunoNaoEncontradoException extends RuntimeException {
    public AlunoNaoEncontradoException(Long idAluno) {
        super(String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, idAluno));
    }
}
