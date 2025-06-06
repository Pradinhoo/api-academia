package com.api_academia.exception.aulapersonal;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MensagensDeErroAulaPersonal {
    public static final String AULA_NAO_ENCONTRADA = "Aula com o ID %s não encontrada!";
    public static final String ERRO_AO_DESMARCAR_AULA = "Aulas só podem ser desmarcadas com mais de 1h de antecedência";
}
