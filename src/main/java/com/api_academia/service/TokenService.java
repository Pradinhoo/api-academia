package com.api_academia.service;

import com.api_academia.model.Usuario;

public interface TokenService {
    String gerarToken(Usuario usuario);
    String getSubject(String tokenJWT);
}
