package com.api_academia.service.impl;

import com.api_academia.dto.ProgressoDTO;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;

import com.api_academia.model.Aluno;
import com.api_academia.model.ClassificacaoIMC;
import com.api_academia.model.Progresso;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.ProgressoRepository;

import com.api_academia.service.ProgressoService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressoServiceImpl implements ProgressoService {

    private final ProgressoRepository progressoRepository;
    private final AlunoRepository alunoRepository;

    public void cadastrarProgressoAluno(Long id, ProgressoDTO dados) {
        Aluno aluno = verificarAlunoPorId(id);

        Double valorImc = calculaIMCAluno(dados.peso(), dados.altura());
        ClassificacaoIMC classificacaoIMC = classificaIMC(valorImc);
        Double pesoIdeal = calculaPesoIdeal(dados.altura());

        Progresso progresso = new Progresso(dados.peso(), dados.altura(), valorImc, pesoIdeal, classificacaoIMC, aluno);
        progressoRepository.save(progresso);
    }

    public List<ProgressoDTO> listarProgressoAluno(Long id) {
        verificarAlunoPorId(id);
        return progressoRepository.listaProgressoAluno(id);
    }

    private Aluno verificarAlunoPorId(Long idAluno) {
        return alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    private Double calculaIMCAluno(Double peso, Double altura) {
        return peso / (altura * altura);
    }

    private ClassificacaoIMC classificaIMC(Double imc) {
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

    private Double calculaPesoIdeal(Double altura) {
        return 27.5 * (altura * altura);
    }
}
