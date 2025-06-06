package com.api_academia.service;

import com.api_academia.dto.AutenticacaoDTO;
import com.api_academia.dto.TokenDTO;

public interface LoginService {
    TokenDTO efetuarLogin(AutenticacaoDTO dados);
}
