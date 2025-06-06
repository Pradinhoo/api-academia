package com.api_academia.exception.professor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MensagensDeErroProfessor {
    public static final String PROFESSOR_NAO_ENCONTRADO = "Professor com o ID %s não encontrado!";
    public static final String PROFESSOR_JA_CADASTRADO = "professor já foi cadastrado anteriormente!";
    public static final String PROFESSOR_JA_DESATIVADO = "Não é possível desativar um professor já desativado!";
    public static final String PROFESSOR_JA_ATIVADO = "Não é possível ativar um professor já ativo!";
}
