package com.api_academia.service.impl;

import com.api_academia.dto.ProgressoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.ClassificacaoIMC;
import com.api_academia.model.Progresso;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.ProgressoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressoServiceImpl {

    @Autowired
    private ProgressoRepository progressoRepository;
    @Autowired
    private AlunoRepository alunoRepository;

    public String cadastrarProgressoAluno(Long id, ProgressoDTO dados) {
        Aluno aluno = alunoRepository.buscaAlunoAtivoPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        Double valorImc = calculaIMCAluno(dados.peso(), dados.altura());
        ClassificacaoIMC classificacaoIMC = classificaIMC(valorImc);
        Double pesoIdeal = calculaPesoIdeal(dados.altura());

        Progresso progresso = new Progresso(dados.peso(), dados.altura(), valorImc, pesoIdeal, classificacaoIMC, aluno);
        progressoRepository.save(progresso);

        return "Progresso gravado com sucesso!";
    }

    public List<ProgressoDTO> listarProgressoAluno(Long id) {
        alunoRepository.buscaAlunoAtivoPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        return progressoRepository.listaProgressoAluno(id);
    }

    public Double calculaIMCAluno(Double peso, Double altura) {
        return peso / (altura * altura);
    }

    public ClassificacaoIMC classificaIMC(Double imc) {
        if (imc < 18.5) {
            return ClassificacaoIMC.MAGREZA;
        } else if (imc < 25) {
            return ClassificacaoIMC.NORMAL;
        } else if (imc < 30) {
            return ClassificacaoIMC.SOBREPESO;
        } else if (imc < 40) {
            return ClassificacaoIMC.OBESIDADE;
        } else {
            return ClassificacaoIMC.OBESIDADE_GRAVE;
        }
    }

    public Double calculaPesoIdeal(Double altura) {
        return 27.5 * (altura * altura);
    }
}
