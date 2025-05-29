package com.api_academia.controller;

import com.api_academia.dto.AutenticacaoDTO;
import com.api_academia.dto.TokenDTO;
import com.api_academia.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@PreAuthorize("hasAnyRole('ADMIN', 'ALUNO', 'PROFESSOR', 'ATENDENTE')")
public class AutenticacaoController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody @Valid AutenticacaoDTO dados) {
        return ResponseEntity.status(HttpStatus.OK).body(loginService.efetuarLogin(dados));
    }
}
