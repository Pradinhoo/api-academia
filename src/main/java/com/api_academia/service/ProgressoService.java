package com.api_academia.service;

import com.api_academia.dto.ProgressoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.Progresso;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;
    @Autowired
    private AlunoRepository alunoRepository;

    public String cadastrarProgressoAluno(Long id, ProgressoDTO dados) {
        Aluno aluno = alunoRepository.buscaAlunoAtivoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Aluno não encontrado"));

        progressoRepository.save(new Progresso(dados, aluno));

        return "Progresso gravado com sucesso!";
    }

    public List<ProgressoDTO> listarProgressoAluno(Long id) {
        alunoRepository.buscaAlunoAtivoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Aluno não encontrado"));
        return progressoRepository.listaProgressoAluno(id);
    }
}
