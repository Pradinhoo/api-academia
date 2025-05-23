package com.api_academia.service;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.model.AulaPersonal;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.AulaPersonalRepository;
import com.api_academia.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AulaPersonalService {

    @Autowired
    private AulaPersonalRepository aulaPersonalRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public String cadastrarAula(AulaPersonalDTO dados) {

        var aluno = alunoRepository.buscaAlunoAtivoPorId(dados.idAluno())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado ou não possui cadastro ativo"));

        var professor = professorRepository.buscaProfessorAtivoPorId(dados.idProfessor())
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado ou não possui cadastro ativo"));

        if (dados.dataHoraAula().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível marcar uma aula no passado");
        }

        var conflitoProfessor = aulaPersonalRepository.verificarConflitoProfessor(
                professor.getId(), dados.dataHoraAula(), dados.dataHoraAulaFim());
        var conflitoAluno = aulaPersonalRepository.verificarConflitoAluno(
                aluno.getId(), dados.dataHoraAula(), dados.dataHoraAulaFim());

        if (conflitoProfessor.isEmpty() && conflitoAluno.isEmpty()) {
            AulaPersonal aula = new AulaPersonal(dados, aluno, professor);
            aulaPersonalRepository.save(aula);
            return "Aula marcada com sucesso";
        } else {
            throw new IllegalStateException("Conflito de horário: professor ou aluno já possui uma aula marcada nesse período");
        }
    }

    public List<AulaPersonalDTO> listarAulasFuturasDoAluno(Long idAluno) {
        alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado ou sem cadastro ativo"));

        LocalDateTime agora = LocalDateTime.now();

        return aulaPersonalRepository.listaAulasMarcadasAlunos(idAluno, agora)
                .stream().toList();
    }

    public List<AulaPersonalDTO> listarAulasFuturasDoProfessor(Long idProfessor) {
        professorRepository.buscaProfessorAtivoPorId(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado ou sem cadastro ativo"));

        LocalDateTime agora = LocalDateTime.now();

        return aulaPersonalRepository.listaAulasMarcadasProfessores(idProfessor, agora)
                .stream().toList();
    }

    public String deletarAula(Long idAula) {
        AulaPersonal aula = aulaPersonalRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada"));

        if (LocalDateTime.now().plusHours(1).isAfter(aula.getDataHoraAula())) {
            throw new IllegalStateException("Aulas só podem ser desmarcadas com mais de 1h de antecedência");
        }

        aulaPersonalRepository.delete(aula);
        return "Aula deletada com sucesso";
    }
}
