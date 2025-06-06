package com.api_academia.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AutenticacaoService {
    UserDetails loadUserByUsername(String username);
}
