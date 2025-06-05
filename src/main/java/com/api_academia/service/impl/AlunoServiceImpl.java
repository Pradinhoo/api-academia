package com.api_academia.service.impl;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.mapper.AlunoMapper;
import com.api_academia.model.Aluno;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.service.AlunoService;
import jakarta.persistence.EntityExistsException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoServiceImpl implements AlunoService {

    private final AlunoMapper alunoMapper;
    private final AlunoRepository alunoRepository;

    public AlunoServiceImpl(AlunoMapper alunoMapper, AlunoRepository alunoRepository) {
        this.alunoMapper = alunoMapper;
        this.alunoRepository = alunoRepository;
    }

    public AlunoDTO cadastrarAluno(AlunoDTO dados) {
        if (alunoRepository.findByCpf(dados.cpf()).isPresent()) {
            throw new EntityExistsException("O aluno já foi cadastrado anteriormente");
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
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno"));
    }

    public AlunoDTO atualizarEnderecoAluno(Long idAluno, AtualizaEnderecoDTO dados) {
        return alunoRepository.findById(idAluno)
                .map(aluno -> {
                    aluno.getEndereco().atualizarEndereco(dados);
                    alunoRepository.save(aluno);
                    return alunoMapper.toDto(aluno);})
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno!"));
    }

    public void desativarAluno(Long idAluno) {
        Aluno aluno = alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno!"));

        aluno.desativarAluno();
        alunoRepository.save(aluno);
    }

    public void ativarAluno(Long idAluno) {
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno!"));

        if (aluno.getCadastroAtivo()) {
            throw new IllegalStateException("Não é possível ativar um aluno já ativo");
        }

        aluno.ativarAluno();
        alunoRepository.save(aluno);
    }

    public AlunoDTO localizarAlunoPorId(Long idAluno) {
        return alunoRepository.findById(idAluno)
                .map(alunoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado!"));
    }
}
