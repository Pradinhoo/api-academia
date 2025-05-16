package com.api_academia.controller;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.service.AulaPersonalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        String mensagem = aulaPersonalService.cadastrarAula(dados);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("/alunos/{idAluno}")
    public ResponseEntity<List<AulaPersonalDTO>> listarAulasFuturasDoAlunos(@PathVariable Long idAluno) {
        List<AulaPersonalDTO> listaDeAulas = aulaPersonalService.listarAulasFuturasDoAluno(idAluno);
        return ResponseEntity.ok(listaDeAulas);
    }

    @GetMapping("/professores/{idProfessor}")
    public ResponseEntity<List<AulaPersonalDTO>> listarAulasFuturasProfessores(@PathVariable Long idProfessor) {
        List<AulaPersonalDTO> listaDeAulas = aulaPersonalService.listarAulasFuturasDoProfessor(idProfessor);
        return ResponseEntity.ok(listaDeAulas);
    }

    @DeleteMapping("/{idAula}")
    public ResponseEntity<String> deletarAulaMarcada(@PathVariable Long idAula) {
        String mensagem = aulaPersonalService.deletarAula(idAula);
        return ResponseEntity.ok(mensagem);
    }
}
