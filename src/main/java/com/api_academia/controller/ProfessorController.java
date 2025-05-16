package com.api_academia.controller;


import com.api_academia.dto.AtualizaProfessorDTO;
import com.api_academia.dto.ProfessorDTO;

import com.api_academia.model.Professor;
import com.api_academia.repository.ProfessorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<ProfessorDTO> cadastrarProfessor(@RequestBody @Valid ProfessorDTO dados) {
        Professor professorCpf = professorRepository.findByCpf(dados.cpf());
        if (professorCpf == null) {
            Professor professor = new Professor(dados);
            professorRepository.save(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ProfessorDTO(professor));
        } else {
            throw new EntityExistsException("CPF já cadastrado no banco!");
        }
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarTodosOsProfessoresAtivos() {
        List<ProfessorDTO> listaProfessoresAtivos = professorRepository.findAllByCadastroAtivoTrue()
                .stream()
                .map(ProfessorDTO::new)
                .toList();
        return ResponseEntity.ok(listaProfessoresAtivos);
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ProfessorDTO> atualizarDadosProfessor(@PathVariable Long id, @RequestBody @Valid AtualizaProfessorDTO dados) {
        return professorRepository.findById(id)
                .map(professor -> {
                    if (dados.nome() != null) professor.setNome(dados.nome());
                    if (dados.email() != null) professor.setEmail(dados.email());
                    if (dados.telefone() != null) professor.setTelefone(dados.telefone());
                    return  ResponseEntity.ok(new ProfessorDTO(professorRepository.save(professor)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<String> desativarProfessor(@PathVariable Long id) {
        var professor = professorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Professor não encontrado!"));
        professor.setCadastroAtivo(false);
        professorRepository.save(professor);
        return ResponseEntity.status(HttpStatus.OK).body("Professor desativado com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarProfessorPorID(@PathVariable Long id) {
        return professorRepository.findById(id)
                .map(professor -> ResponseEntity.ok(new ProfessorDTO(professor)))
                .orElse(ResponseEntity.notFound().build());

    }
}
