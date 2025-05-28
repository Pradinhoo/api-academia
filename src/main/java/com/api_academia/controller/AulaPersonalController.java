package com.api_academia.controller;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.service.AulaPersonalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aulas")
@PreAuthorize("hasAnyRole('ADMIN', 'ALUNO', 'PROFESSOR', 'ATENDENTE')")
public class AulaPersonalController {

    @Autowired
    AulaPersonalService aulaPersonalService;

    @PostMapping
    public ResponseEntity<String> cadastrarAula(@RequestBody @Valid AulaPersonalDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaPersonalService.cadastrarAula(dados));
    }

    @GetMapping
    public ResponseEntity<List<AulaPersonalDTO>> listarTodasAsAulasFuturas() {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.listarTodasAsAulasFuturas());
    }

    @GetMapping("/{id}/aluno")
    public ResponseEntity<List<AulaPersonalDTO>> listarAulasFuturasDoAlunos(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.listarAulasFuturasDoAluno(id));
    }

    @GetMapping("/{id}/professor")
    public ResponseEntity<List<AulaPersonalDTO>> listarAulasFuturasProfessores(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.listarAulasFuturasDoProfessor(id));
    }

    @DeleteMapping("/{id}/deletar")
    public ResponseEntity<String> deletarAulaMarcada(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.deletarAula(id));
    }
}
