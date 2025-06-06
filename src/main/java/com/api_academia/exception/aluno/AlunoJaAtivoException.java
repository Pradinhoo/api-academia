package com.api_academia.exception.aluno;

public class AlunoJaAtivoException extends RuntimeException {
    public AlunoJaAtivoException(Long idAluno) {
        super(String.format(MensagensDeErroAluno.ALUNO_JA_ATIVADO, idAluno));
    }
}
