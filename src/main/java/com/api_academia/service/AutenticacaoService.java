package com.api_academia.service;

import com.api_academia.model.Usuario;
import com.api_academia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails usuario = usuarioRepository.localizarUsuarioPorUsername(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com login: " + username);
        }

        return usuario;
    }
}
