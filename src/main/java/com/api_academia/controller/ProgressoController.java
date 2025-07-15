package com.api_academia.controller;

import com.api_academia.dto.ProgressoDTO;
import com.api_academia.service.ProgressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progresso")
@PreAuthorize("hasAnyRole('ADMIN', 'ALUNO', 'PROFESSOR')")
@RequiredArgsConstructor
public class ProgressoController {

    private final ProgressoService progressoService;

    @PostMapping("/{id}/cadastrar")
    public ResponseEntity<String> cadastrarProgressoAluno(@PathVariable Long id, @RequestBody ProgressoDTO dados) {
        progressoService.cadastrarProgressoAluno(id, dados);
        return ResponseEntity.status(HttpStatus.CREATED).body("Progresso gravado com sucesso!");
    }

    @GetMapping("/{id}/listar")
    public ResponseEntity<List<ProgressoDTO>> listarProgressoAluno(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(progressoService.listarProgressoAluno(id));
    }
}
