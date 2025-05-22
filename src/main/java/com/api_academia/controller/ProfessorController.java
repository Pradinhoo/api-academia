package com.api_academia.controller;


import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.dto.AtualizaProfessorDTO;
import com.api_academia.dto.ProfessorDTO;

import com.api_academia.model.Professor;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.ProfessorService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ProfessorDTO> cadastrarProfessor(@RequestBody @Valid ProfessorDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.cadastrarProfessor(dados));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarTodosOsProfessoresAtivos() {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.listarProfessoresAtivos());
    }

    @PatchMapping("/{id}/atualizar-dados")
    public ResponseEntity<ProfessorDTO> atualizarDadosProfessor(@PathVariable Long id, @RequestBody @Valid AtualizaProfessorDTO dados) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.atualizarDadosProfessor(id, dados));
    }

    @PatchMapping("/{id}/atualizar-endereco")
    public ResponseEntity<ProfessorDTO> atualizarEnderecoProfessor(@PathVariable Long id, @RequestBody @Valid AtualizaEnderecoDTO dados) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.atualizarEnderecoProfessor(id, dados));
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<String> desativarProfessor(@PathVariable Long id) {
        professorService.desativarProfessor(id);
        return ResponseEntity.status(HttpStatus.OK).body("Professor desativado com sucesso");
    }

    @PostMapping("/ativar/{id}")
    public ResponseEntity<String> ativarProfessor(@PathVariable Long id) {
        professorService.ativarProfessor(id);
        return ResponseEntity.status(HttpStatus.OK).body("Professor ativado com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarProfessorPorID(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.buscarProfessorPorId(id));
    }
}
