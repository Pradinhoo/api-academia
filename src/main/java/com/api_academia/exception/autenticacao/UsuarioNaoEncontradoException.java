package com.api_academia.exception.autenticacao;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super(MensagensDeErroAutenticao.USUARIO_NAO_ENCONTRADO);
    }
}
