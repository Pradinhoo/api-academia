package com.api_academia.exception.checkin;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MensagensDeErroCheckIn {
    public static final String CPF_NAO_ENCONTRADO = "Nenhum aluno cadastrado com o CPF: %s";
    public static final String ERRO_AO_REALIZAR_CHECKIN = "Somente um Check-In pode ser realizado por dia";
}
