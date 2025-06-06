package com.api_academia.exception.aluno;

public class AlunoJaCadastradoException extends RuntimeException {
    public AlunoJaCadastradoException(String cpfAluno) {
        super(String.format(MensagensDeErroAluno.ALUNO_JA_CADASTRADO_ANTERIORMENTE, cpfAluno));
    }
}
