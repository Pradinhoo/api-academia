package com.api_academia.service.impl;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.AulaPersonal;
import com.api_academia.model.Professor;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.AulaPersonalRepository;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.AulaPersonalService;
import com.api_academia.validations.ValidarCadastroAula;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AulaPersonalServiceImpl implements AulaPersonalService {

    private final AulaPersonalRepository aulaPersonalRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final List<ValidarCadastroAula> validadores;

    public AulaPersonalServiceImpl(AulaPersonalRepository aulaPersonalRepository, AlunoRepository alunoRepository, ProfessorRepository professorRepository, List<ValidarCadastroAula> validadores) {
        this.aulaPersonalRepository = aulaPersonalRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.validadores = validadores;
    }

    public String cadastrarAula(AulaPersonalDTO dados) {

        validadores.forEach(v -> v.validar(dados));
        Aluno aluno = validarAluno(dados.idAluno());
        Professor professor = validarProfessor(dados.idProfessor());

        AulaPersonal aula = new AulaPersonal(dados, aluno, professor);
        aulaPersonalRepository.save(aula);
        return "Aula marcada com sucesso";
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

    public String deletarAula(Long idAula) {
        AulaPersonal aula = aulaPersonalRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada"));

        if (LocalDateTime.now().plusHours(1).isAfter(aula.getDataHoraAula())) {
            throw new IllegalStateException("Aulas só podem ser desmarcadas com mais de 1h de antecedência");
        }

        aulaPersonalRepository.delete(aula);
        return "Aula deletada com sucesso";
    }

    private Aluno validarAluno(Long idAluno) {
        return alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado ou não possui cadastro ativo"));
    }

    private Professor validarProfessor (Long idProfessor) {
        return professorRepository.buscaProfessorAtivoPorId(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado ou não possui cadastro ativo"));
    }
}
