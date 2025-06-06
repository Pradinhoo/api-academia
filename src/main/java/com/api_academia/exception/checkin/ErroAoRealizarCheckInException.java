package com.api_academia.exception.checkin;

public class ErroAoRealizarCheckInException extends RuntimeException {
    public ErroAoRealizarCheckInException() {
        super(MensagensDeErroCheckIn.ERRO_AO_REALIZAR_CHECKIN);
    }
}
