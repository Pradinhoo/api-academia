package com.api_academia.exception.aulapersonal;

public class MensagensDeErroAulaPersonal {
    public static final String AULA_NAO_ENCONTRADA = "Aula com o ID %s não encontrada!";
    public static final String ERRO_AO_DESMARCAR_AULA = "Aulas só podem ser desmarcadas com mais de 1h de antecedência";
    public static final String ERRO_AO_VALIDAR_AULA_NO_PASSADO = "Não é possível maarcar uma aula no passado!";
    public static final String ERRO_CONFLITO_PROFESSOR = "Conflito de horário: PROFESSOR já possui uma aula marcada nesse horário!";
    public static final String ERRO_CONFLITO_ALUNO = "Conflito de horário: ALUNO já possui uma aula marcada nesse horário!";

    private MensagensDeErroAulaPersonal() {}
}
