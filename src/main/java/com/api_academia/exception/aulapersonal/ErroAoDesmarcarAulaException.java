package com.api_academia.exception.aulapersonal;

public class ErroAoDesmarcarAulaException extends RuntimeException {
    public ErroAoDesmarcarAulaException() {
        super(MensagensDeErroAulaPersonal.ERRO_AO_DESMARCAR_AULA);
    }
}
