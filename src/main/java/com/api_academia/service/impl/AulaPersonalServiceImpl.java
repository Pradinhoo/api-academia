package com.api_academia.service.impl;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.model.AulaPersonal;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.AulaPersonalRepository;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.AulaPersonalService;
import com.api_academia.validations.ValidarCadastroAula;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

        var aluno = alunoRepository.buscaAlunoAtivoPorId(dados.idAluno())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado ou não possui cadastro ativo"));

        var professor = professorRepository.buscaProfessorAtivoPorId(dados.idProfessor())
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado ou não possui cadastro ativo"));

        AulaPersonal aula = new AulaPersonal(dados, aluno, professor);
        aulaPersonalRepository.save(aula);
        return "Aula marcada com sucesso";
    }

    public List<AulaPersonalDTO> listarTodasAsAulasFuturas() {
        return aulaPersonalRepository.listaTodasAsAulasFuturas();
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
