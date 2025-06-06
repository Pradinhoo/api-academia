package com.api_academia.service.impl;

import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.dto.AtualizaProfessorDTO;
import com.api_academia.dto.ProfessorDTO;
import com.api_academia.exception.professor.ProfessorJaAtivadoException;
import com.api_academia.exception.professor.ProfessorJaCadastradoException;
import com.api_academia.exception.professor.ProfessorJaDesativadoException;
import com.api_academia.exception.professor.ProfessorNaoEncontradoException;
import com.api_academia.mapper.ProfessorMapper;
import com.api_academia.model.Professor;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ProfessorMapper professorMapper;

    public ProfessorDTO cadastrarProfessor(ProfessorDTO dados) {
        verificaProfessorPorCpf(dados.cpf());
        Professor professor = professorMapper.toEntity(dados);
        professorRepository.save(professor);
        return professorMapper.toDto(professor);
    }

    public List<ProfessorDTO> listarProfessoresAtivos() {
        return professorRepository.findAllByCadastroAtivoTrue()
                .stream()
                .map(professorMapper::toDto)
                .toList();
    }

    public ProfessorDTO atualizarDadosProfessor(Long idProfessor, AtualizaProfessorDTO dados) {
        return professorRepository.buscaProfessorAtivoPorId(idProfessor)
                .map(professor -> {

                    professor.atualizarDadosProfessor(dados);
                    professorRepository.save(professor);

                    return professorMapper.toDto(professor);})
                .orElseThrow(() -> new ProfessorNaoEncontradoException(idProfessor));
    }

    public ProfessorDTO atualizarEnderecoProfessor(Long idProfessor, AtualizaEnderecoDTO dados) {
        return professorRepository.findById(idProfessor)
                .map(professor -> {

                    professor.getEndereco().atualizarEndereco(dados);
                    professorRepository.save(professor);

                    return professorMapper.toDto(professor);})
                .orElseThrow(() -> new ProfessorNaoEncontradoException(idProfessor));
    }

    public void desativarProfessor(Long idProfessor) {
        Professor professor = verificaProfessorPorId(idProfessor);
        boolean cadastroProfessor = professor.getCadastroAtivo();
        if(!cadastroProfessor) {
            throw new ProfessorJaDesativadoException();
        }
        professor.desativarCadastroProfessor();
        professorRepository.save(professor);
    }

    public void ativarProfessor(Long idProfessor) {
        Professor professor = verificaProfessorPorId(idProfessor);
        boolean cadastroProfessor = professor.getCadastroAtivo();
        if (cadastroProfessor) {
            throw new ProfessorJaAtivadoException();
        }

        professor.ativarCadastroProfessor();
        professorRepository.save(professor);
    }

    public ProfessorDTO buscarProfessorPorId(Long idProfessor) {
        return professorRepository.findById(idProfessor)
                .map(professorMapper::toDto)
                .orElseThrow(() -> new ProfessorNaoEncontradoException(idProfessor));
    }

    private void verificaProfessorPorCpf(String cpfProfessor) {
        if (professorRepository.findByCpf(cpfProfessor).isPresent()) {
            throw new ProfessorJaCadastradoException();
        }
    }

    private Professor verificaProfessorPorId(Long idProfessor) {
         return professorRepository.findById(idProfessor)
                .orElseThrow(() -> new ProfessorNaoEncontradoException(idProfessor));
    }
}
