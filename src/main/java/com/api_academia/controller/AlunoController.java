package com.api_academia.controller;


import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoDTO> cadastrarAluno(@RequestBody @Valid AlunoDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.cadastrarAluno(dados));
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodosOsAlunosAtivos() {
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.listarTodosOsAlunoAtivos());
    }

    @PatchMapping("/{id}/atualizar-dados")
    public ResponseEntity<AlunoDTO> atualizarDadosAluno(@PathVariable Long id, @RequestBody @Valid AtualizaAlunoDTO dados) {
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.atualizarDadosAluno(id, dados));
    }

    @PatchMapping("/{id}/atualizar-endereco")
    public ResponseEntity<AlunoDTO> atualizarEnderecoAluno(@PathVariable Long id, @RequestBody @Valid AtualizaEnderecoDTO dados) {
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.atualizarEnderecoAluno(id, dados));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<String> desativarAluno(@PathVariable Long id) {
        alunoService.desativarAluno(id);
        return ResponseEntity.status(HttpStatus.OK).body("Aluno desativado com sucesso");
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<String> ativarAluno(@PathVariable Long id) {
        alunoService.ativarAluno(id);
        return ResponseEntity.status(HttpStatus.OK).body("Aluno ativado com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> localizarAlunoPorID(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.localizarAlunoPorId(id));
    }
}
