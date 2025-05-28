package com.api_academia;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.AulaPersonal;
import com.api_academia.model.Professor;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.AulaPersonalRepository;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.AulaPersonalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AulaPersonalServiceTest {

    @InjectMocks
    private AulaPersonalService service;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private AulaPersonalRepository aulaPersonalRepository;

    @Test
    void deveCadastrarAulaComSucesso() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        Aluno aluno = new Aluno();
        aluno.setId(idAluno);
        Professor professor = new Professor();
        professor.setId(idProfessor);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));

        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.of(professor));

        when(aulaPersonalRepository.verificarConflitoProfessor(idProfessor, dataHoraAula, dataHoraAulaFim))
                .thenReturn(Collections.emptyList());
        when(aulaPersonalRepository.verificarConflitoAluno(idAluno, dataHoraAula, dataHoraAulaFim))
                .thenReturn(Collections.emptyList());

        String resultado = service.cadastrarAula(dto);

        assertEquals("Aula marcada com sucesso", resultado);
        verify(aulaPersonalRepository, times(1)).save(any(AulaPersonal.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontrado() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.cadastrarAula(dto));
        assertEquals("Aluno não encontrado ou não possui cadastro ativo", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontrado() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);
        Aluno aluno = new Aluno();
        aluno.setId(idAluno);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));

        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.cadastrarAula(dto));
        assertEquals("Professor não encontrado ou não possui cadastro ativo", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAulaForMarcadaNoPassado() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().minusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        Aluno aluno = new Aluno();
        aluno.setId(idAluno);
        Professor professor = new Professor();
        professor.setId(idProfessor);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));

        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.of(professor));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarAula(dto));
        assertEquals("Não é possível marcar uma aula no passado", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAlunoTiverConflitoDeHorario() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        Aluno aluno = new Aluno();
        aluno.setId(idAluno);
        Professor professor = new Professor();
        professor.setId(idProfessor);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));

        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.of(professor));

        when(aulaPersonalRepository.verificarConflitoProfessor(idProfessor, dataHoraAula, dataHoraAulaFim))
                .thenReturn(Collections.emptyList());
        when(aulaPersonalRepository.verificarConflitoAluno(idAluno, dataHoraAula, dataHoraAulaFim))
                .thenReturn(List.of(dto));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.cadastrarAula(dto));
        assertEquals("Conflito de horário: professor ou aluno já possui uma aula marcada nesse período", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoProfessorTiverConflitoDeHorario() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        Aluno aluno = new Aluno();
        aluno.setId(idAluno);
        Professor professor = new Professor();
        professor.setId(idProfessor);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));

        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.of(professor));

        when(aulaPersonalRepository.verificarConflitoProfessor(idProfessor, dataHoraAula, dataHoraAulaFim))
                .thenReturn(List.of(dto));
        when(aulaPersonalRepository.verificarConflitoAluno(idAluno, dataHoraAula, dataHoraAulaFim))
                .thenReturn(Collections.emptyList());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.cadastrarAula(dto));
        assertEquals("Conflito de horário: professor ou aluno já possui uma aula marcada nesse período", ex.getMessage());
    }

    @Test
    void deveListarTodasAsAulasFuturasComAulas() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        when(aulaPersonalRepository.listaTodasAsAulasFuturas()).thenReturn(List.of(dto));

        List<AulaPersonalDTO> resultado = service.listarTodasAsAulasFuturas();

        assertNotNull(resultado);
        verify(aulaPersonalRepository, times(1)).listaTodasAsAulasFuturas();
    }

    @Test
    void deveListarTodasAsAulasFuturasSemAulas() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        when(aulaPersonalRepository.listaTodasAsAulasFuturas()).thenReturn(Collections.emptyList());

        List<AulaPersonalDTO> resultado = service.listarTodasAsAulasFuturas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(aulaPersonalRepository, times(1)).listaTodasAsAulasFuturas();
    }

    @Test
    void deveListaAsAulasFuturasDoAlunoComSucesso() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        Aluno aluno = new Aluno();
        aluno.setId(idAluno);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));
        when(aulaPersonalRepository.listaAulasMarcadasAlunos(eq(idAluno), any(LocalDateTime.class))).thenReturn(List.of(dto));

        List<AulaPersonalDTO> resultado = service.listarAulasFuturasDoAluno(idAluno);

        assertNotNull(resultado);
    }

    @Test
    void deveListaAsAulasFuturasDoProfessorComSucesso() {
        Long idAluno = 1L;
        Long idProfessor = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(1);
        LocalDateTime dataHoraAulaFim = dataHoraAula.plusHours(1);

        AulaPersonalDTO dto = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);

        Professor professor = new Professor();
        professor.setId(idProfessor);

        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.of(professor));
        when(aulaPersonalRepository.listaAulasMarcadasProfessores(eq(idProfessor), any(LocalDateTime.class))).thenReturn(List.of(dto));

        List<AulaPersonalDTO> resultado = service.listarAulasFuturasDoProfessor(idProfessor);

        assertNotNull(resultado);
    }

    @Test
    void deveDeletarAulaMarcadaComSucesso() {
        Long idAulaPersonal = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusHours(2);

        AulaPersonal aulaPersonal = new AulaPersonal();
        aulaPersonal.setId(idAulaPersonal);
        aulaPersonal.setDataHoraAula(dataHoraAula);

        when(aulaPersonalRepository.findById(idAulaPersonal)).thenReturn(Optional.of(aulaPersonal));

        String resultado = service.deletarAula(idAulaPersonal);

        assertEquals("Aula deletada com sucesso", resultado);
        verify(aulaPersonalRepository, times(1)).delete(any(AulaPersonal.class));
    }

    @Test
    void deveLancarExcecaoQuandoAulaNaoEncontrada() {
        Long idAulaPersonal = 1L;

        when(aulaPersonalRepository.findById(idAulaPersonal)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.deletarAula(idAulaPersonal));
        assertEquals("Aula não encontrada", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAulaForDentroDeUmaHora() {
        Long idAulaPersonal = 1L;
        LocalDateTime dataHoraAula = LocalDateTime.now().plusMinutes(30);

        AulaPersonal aulaPersonal = new AulaPersonal();
        aulaPersonal.setId(idAulaPersonal);
        aulaPersonal.setDataHoraAula(dataHoraAula);

        when(aulaPersonalRepository.findById(idAulaPersonal)).thenReturn(Optional.of(aulaPersonal));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.deletarAula(idAulaPersonal));

        assertEquals("Aulas só podem ser desmarcadas com mais de 1h de antecedência", ex.getMessage());
        verify(aulaPersonalRepository, never()).delete(any());
    }
}
