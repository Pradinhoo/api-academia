package com.api_academia.controller;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.service.AulaPersonalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aulas")
public class AulaPersonalController {

    @Autowired
    AulaPersonalService aulaPersonalService;

    @PostMapping
    public ResponseEntity<String> cadastrarAula(@RequestBody @Valid AulaPersonalDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaPersonalService.cadastrarAula(dados));
    }

    @GetMapping("/{id}/aluno")
    public ResponseEntity<List<AulaPersonalDTO>> listarAulasFuturasDoAlunos(@PathVariable Long idAluno) {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.listarAulasFuturasDoAluno(idAluno));
    }

    @GetMapping("/{id}/professor")
    public ResponseEntity<List<AulaPersonalDTO>> listarAulasFuturasProfessores(@PathVariable Long idProfessor) {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.listarAulasFuturasDoProfessor(idProfessor));
    }

    @DeleteMapping("/{idAula}/deletar")
    public ResponseEntity<String> deletarAulaMarcada(@PathVariable Long idAula) {
        return ResponseEntity.status(HttpStatus.OK).body(aulaPersonalService.deletarAula(idAula));
    }
}
