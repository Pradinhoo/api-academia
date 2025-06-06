package com.api_academia.exception.aluno;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MensagensDeErroAluno {
    public static final String ALUNO_NAO_ENCONTRADO = "Aluno com ID %s não encontrado!";
    public static final String ALUNO_JA_ATIVADO = "Aluno com ID %s já possui cadastro ativo!";
    public static final String ALUNO_JA_DESATIVADO = "Aluno com ID %s já possui cadastro desativado";
    public static final String ALUNO_JA_CADASTRADO_ANTERIORMENTE = "Aluno com CPF %s já possui cadastro!";
}
