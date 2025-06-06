package com.api_academia.service.impl;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.exception.aulapersonal.AulaPersonalNaoEncontradaException;
import com.api_academia.exception.aulapersonal.ErroAoDesmarcarAulaException;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.exception.professor.ProfessorNaoEncontradoException;
import com.api_academia.model.Aluno;
import com.api_academia.model.AulaPersonal;
import com.api_academia.model.Professor;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.AulaPersonalRepository;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.AulaPersonalService;
import com.api_academia.validations.ValidarCadastroAula;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AulaPersonalServiceImpl implements AulaPersonalService {

    private final AulaPersonalRepository aulaPersonalRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final List<ValidarCadastroAula> validadores;

    public void cadastrarAula(AulaPersonalDTO dados) {

        validadores.forEach(v -> v.validar(dados));
        Aluno aluno = validarAluno(dados.idAluno());
        Professor professor = validarProfessor(dados.idProfessor());

        AulaPersonal aula = new AulaPersonal(aluno, professor, dados.dataHoraAula());
        aulaPersonalRepository.save(aula);
    }

    public List<AulaPersonalDTO> listarTodasAsAulasFuturas() {
        return aulaPersonalRepository.listaTodasAsAulasFuturas();
    }

    public List<AulaPersonalDTO> listarAulasFuturasDoAluno(Long idAluno) {
        validarAluno(idAluno);
        return aulaPersonalRepository.listaAulasMarcadasAlunos(idAluno, LocalDateTime.now())
                .stream().toList();
    }

    public List<AulaPersonalDTO> listarAulasFuturasDoProfessor(Long idProfessor) {
        validarProfessor(idProfessor);
        return aulaPersonalRepository.listaAulasMarcadasProfessores(idProfessor, LocalDateTime.now())
                .stream().toList();
    }

    public void deletarAula(Long idAula) {
        AulaPersonal aula = aulaPersonalRepository.findById(idAula)
                .orElseThrow(() -> new AulaPersonalNaoEncontradaException(idAula));

        if (LocalDateTime.now().plusHours(1).isAfter(aula.getDataHoraAula())) {
            throw new ErroAoDesmarcarAulaException();
        }

        aulaPersonalRepository.delete(aula);
    }

    private Aluno validarAluno(Long idAluno) {
        return alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    private Professor validarProfessor (Long idProfessor) {
        return professorRepository.buscaProfessorAtivoPorId(idProfessor)
                .orElseThrow(() -> new ProfessorNaoEncontradoException(idProfessor));
    }
}
