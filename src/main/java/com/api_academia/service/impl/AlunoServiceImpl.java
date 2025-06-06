package com.api_academia.service.impl;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.exception.aluno.AlunoJaAtivoException;
import com.api_academia.exception.aluno.AlunoJaCadastradoException;
import com.api_academia.exception.aluno.AlunoJaDesativadoException;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.mapper.AlunoMapper;
import com.api_academia.model.Aluno;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.service.AlunoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService {

    private final AlunoMapper alunoMapper;
    private final AlunoRepository alunoRepository;

    public AlunoDTO cadastrarAluno(AlunoDTO dados) {
        if (alunoRepository.findByCpf(dados.cpf()).isPresent()) {
            throw new AlunoJaCadastradoException(dados.cpf());
        }

        Aluno alunoEntity = alunoMapper.toEntity(dados);
        alunoRepository.save(alunoEntity);

        return alunoMapper.toDto(alunoEntity);
    }

    public List<AlunoDTO> listarTodosOsAlunoAtivos() {
        return alunoRepository.findAllByCadastroAtivoTrue()
                .stream()
                .map(alunoMapper::toDto)
                .toList();
    }

    public AlunoDTO atualizarDadosAluno(Long idAluno, AtualizaAlunoDTO dados) {
        return alunoRepository.findById(idAluno)
                .map(aluno -> {

                    aluno.atualizaDadosAluno(dados);
                    alunoRepository.save(aluno);

                    return alunoMapper.toDto(aluno);})
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    public AlunoDTO atualizarEnderecoAluno(Long idAluno, AtualizaEnderecoDTO dados) {
        return alunoRepository.findById(idAluno)
                .map(aluno -> {

                    aluno.getEndereco().atualizarEndereco(dados);
                    alunoRepository.save(aluno);

                    return alunoMapper.toDto(aluno);})
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    public void desativarAluno(Long idAluno) {
        Aluno aluno = buscarAlunoPorId(idAluno);

        boolean cadastroAluno = aluno.getCadastroAtivo();
        if (!cadastroAluno) {
            throw new AlunoJaDesativadoException(idAluno);
        }

        aluno.desativarCadastroAluno();
        alunoRepository.save(aluno);
    }

    public void ativarAluno(Long idAluno) {
        Aluno aluno = buscarAlunoPorId(idAluno);

        boolean cadastroAluno = aluno.getCadastroAtivo();
        if (cadastroAluno) {
            throw new AlunoJaAtivoException(idAluno);
        }

        aluno.ativarCadastroAluno();
        alunoRepository.save(aluno);
    }

    public AlunoDTO localizarAlunoPorId(Long idAluno) {
        return alunoRepository.findById(idAluno)
                .map(alunoMapper::toDto)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    private Aluno buscarAlunoPorId(Long idAluno) {
        return alunoRepository.findById(idAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }
}
