package com.api_academia.service.impl;

import com.api_academia.exception.autenticacao.UsuarioNaoEncontradoException;
import com.api_academia.repository.UsuarioRepository;
import com.api_academia.service.AutenticacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails usuario = usuarioRepository.localizarUsuarioPorUsername(username);

        if (usuario == null) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuario;
    }
}
