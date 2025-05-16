package com.api_academia.controller;


import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.repository.AlunoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @PostMapping
    public ResponseEntity<AlunoDTO> cadastrarAluno(@RequestBody @Valid AlunoDTO dados) {
        if (alunoRepository.findByCpf(dados.cpf()) ==  null) {
            Aluno aluno = new Aluno(dados);
            alunoRepository.save(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AlunoDTO(aluno));
        } else {
            throw new EntityExistsException("CPF já cadastrado no banco!");
        }
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodosOsAlunosAtivos() {
        List<AlunoDTO> listaDeAlunos = alunoRepository.findAllByCadastroAtivoTrue()
                .stream()
                .map(AlunoDTO::new)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(listaDeAlunos);
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<Aluno> atualizarDadosAlunos(@PathVariable Long id, @RequestBody @Valid AtualizaAlunoDTO dados) {
        return alunoRepository.findById(id)
                .map(aluno -> {
                    if (dados.nome() != null) aluno.setNome(dados.nome());
                    if (dados.email() != null) aluno.setEmail(dados.email());
                    if (dados.telefone() != null) aluno.setTelefone(dados.telefone());
                    if (dados.cep() != null) aluno.setCep(dados.cep());
                    return  ResponseEntity.ok(alunoRepository.save(aluno));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<String> desativarAluno(@PathVariable Long id) {
        var aluno = alunoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado!"));
        aluno.setCadastroAtivo(false);
        alunoRepository.save(aluno);
        return ResponseEntity.status(HttpStatus.OK).body("Aluno desativado com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> localizarAlunoPorID(@PathVariable Long id) {
        return alunoRepository.findById(id)
                .map(aluno -> ResponseEntity.ok(new AlunoDTO(aluno)))
                .orElse(ResponseEntity.notFound().build());
    }
}
