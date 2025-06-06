package com.api_academia.exception.aulapersonal;

public class AulaPersonalNaoEncontradaException extends RuntimeException {
    public AulaPersonalNaoEncontradaException(Long idAulaPersonal) {
        super(String.format(MensagensDeErroAulaPersonal.AULA_NAO_ENCONTRADA, idAulaPersonal));
    }
}
