package com.api_academia.controller;

import com.api_academia.dto.ProgressoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.Progresso;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/progresso")
public class ProgressoController {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private ProgressoRepository progressoRepository;

    @PostMapping("/cadastrar/{id}")
    public ResponseEntity<String> cadastrarProgressoAluno(@PathVariable Long id, @RequestBody ProgressoDTO dados) {
        Aluno aluno = alunoRepository.buscaAlunoAtivoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Aluno não encontrado"));

        Progresso progresso = new Progresso(dados, aluno);
        progressoRepository.save(progresso);

        return ResponseEntity.status(HttpStatus.CREATED).body("Progresso gravado com sucesso");
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<List<ProgressoDTO>> listarProgressoAluno(@PathVariable Long id) {
        Aluno aluno = alunoRepository.buscaAlunoAtivoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Aluno não encontrado"));

        List<ProgressoDTO> listaProgresso = progressoRepository.listaProgressoAluno(id);

        return ResponseEntity.status(HttpStatus.OK).body(listaProgresso);
    }
}
