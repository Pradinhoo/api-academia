package com.api_academia.service.impl;

import com.api_academia.dto.AutenticacaoDTO;
import com.api_academia.dto.TokenDTO;
import com.api_academia.model.Usuario;
import com.api_academia.service.LoginService;
import com.api_academia.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public TokenDTO efetuarLogin(AutenticacaoDTO dados) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return new TokenDTO(tokenJWT);
    }
}
