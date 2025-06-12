package com.api_academia;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.exception.aluno.MensagensDeErroAluno;
import com.api_academia.exception.aulapersonal.AulaPersonalNaoEncontradaException;
import com.api_academia.exception.aulapersonal.MensagensDeErroAulaPersonal;
import com.api_academia.exception.professor.MensagensDeErroProfessor;
import com.api_academia.exception.professor.ProfessorNaoEncontradoException;
import com.api_academia.model.Aluno;
import com.api_academia.model.AulaPersonal;
import com.api_academia.model.Professor;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.AulaPersonalRepository;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.impl.AulaPersonalServiceImpl;
import com.api_academia.validations.ValidarCadastroAula;
import com.api_academia.validations.ValidarConflitoAluno;
import com.api_academia.validations.ValidarConflitoProfessor;
import com.api_academia.validations.ValidarHorarioAulaNoPassado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AulaPersonalServiceTest {

    private AulaPersonalServiceImpl service;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private AulaPersonalRepository aulaPersonalRepository;
    @Mock
    private ValidarConflitoAluno validarConflitoAluno;
    @Mock
    private ValidarConflitoProfessor validarConflitoProfessor;
    @Mock
    private ValidarHorarioAulaNoPassado validarHorarioAulaNoPassado;

    private Long idAluno;
    private Long idProfessor;
    private LocalDateTime dataHoraAula;
    private LocalDateTime dataHoraAulaFim;
    private Aluno aluno;
    private Professor professor;
    private AulaPersonalDTO aulaPersonalDTO;
    private AulaPersonal aulaPersonal;
    private List<ValidarCadastroAula> listaDeValidadores;

    @BeforeEach
    void setUp() {
        idAluno = 1L;
        idProfessor = 1L;
        dataHoraAula = LocalDateTime.now().plusHours(1);
        dataHoraAulaFim = dataHoraAula.plusHours(1);
        aluno = new Aluno();
        aluno.cadastrarIdAluno(idAluno);
        professor = new Professor();
        professor.cadastrarIdProfessor(idProfessor);
        aulaPersonalDTO = new AulaPersonalDTO(idAluno, idProfessor, dataHoraAula, dataHoraAulaFim);
        aulaPersonal = new AulaPersonal(aluno, professor, dataHoraAula);

        listaDeValidadores = Arrays.asList(
                validarConflitoAluno,
                validarConflitoProfessor,
                validarHorarioAulaNoPassado);

        service = new AulaPersonalServiceImpl(
                aulaPersonalRepository,
                alunoRepository,
                professorRepository,
                listaDeValidadores
        );
    }

    @Test
    void deveCadastrarAulaComSucesso() {

        doNothing().when(validarConflitoAluno).validar(aulaPersonalDTO);
        doNothing().when(validarConflitoProfessor).validar(aulaPersonalDTO);
        doNothing().when(validarHorarioAulaNoPassado).validar(aulaPersonalDTO);

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));
        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.of(professor));

        service.cadastrarAula(aulaPersonalDTO);

        verify(validarConflitoAluno, times(1)).validar(aulaPersonalDTO);
        verify(validarConflitoProfessor, times(1)).validar(aulaPersonalDTO);
        verify(validarHorarioAulaNoPassado, times(1)).validar(aulaPersonalDTO);

        verify(aulaPersonalRepository, times(1)).save(any(AulaPersonal.class));
        verify(alunoRepository, never()).save(any(Aluno.class));
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontrado() {

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class,
                () -> service.cadastrarAula(aulaPersonalDTO));
        assertEquals(String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, idAluno), ex.getMessage());

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(idAluno);
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontrado() {

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));
        when(professorRepository.buscaProfessorAtivoPorId(idProfessor)).thenReturn(Optional.empty());

        ProfessorNaoEncontradoException ex = assertThrows(ProfessorNaoEncontradoException.class,
                () -> service.cadastrarAula(aulaPersonalDTO));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, idProfessor), ex.getMessage());

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(idAluno);
        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(idProfessor);
        verify(aulaPersonalRepository, never()).save(any(AulaPersonal.class));
    }

    @Test
    void deveListarTodasAsAulasFuturasComAulas() {
        when(aulaPersonalRepository.listaTodasAsAulasFuturas()).thenReturn(List.of(aulaPersonalDTO));

        List<AulaPersonalDTO> resultado = service.listarTodasAsAulasFuturas();

        assertNotNull(resultado);
        verify(aulaPersonalRepository, times(1)).listaTodasAsAulasFuturas();
    }

    @Test
    void deveListarTodasAsAulasFuturasSemAulas() {

        when(aulaPersonalRepository.listaTodasAsAulasFuturas()).thenReturn(Collections.emptyList());

        List<AulaPersonalDTO> resultado = service.listarTodasAsAulasFuturas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(aulaPersonalRepository, times(1)).listaTodasAsAulasFuturas();
    }

    @Test
    void deveListarAsAulasFuturasDoAlunoComSucesso() {

        when(alunoRepository.buscaAlunoAtivoPorId(idAluno)).thenReturn(Optional.of(aluno));
        when(aulaPersonalRepository.listaAulasMarcadasAlunos(eq(idAluno), any(LocalDateTime.class))).thenReturn(List.of(aulaPersonalDTO));

        List<AulaPersonalDTO> resultado = service.listarAulasFuturasDoAluno(idAluno);

        assertNotNull(resultado);
    }

    @Test
    void deveDeletarAulaMarcadaComSucesso() {
        Long idAulaPersonal = 1L;
        AulaPersonal aulaFutura = new AulaPersonal(aluno, professor, LocalDateTime.now().plusHours(2));

        when(aulaPersonalRepository.findById(idAulaPersonal)).thenReturn(Optional.of(aulaFutura));

        service.deletarAula(idAulaPersonal);

        verify(aulaPersonalRepository, times(1)).findById(idAulaPersonal);
        verify(aulaPersonalRepository, times(1)).delete(any(AulaPersonal.class));
    }

    @Test
    void deveLancarExcecaoQuandoAulaNaoEncontrada() {
        Long idAulaPersonal = 1L;

        when(aulaPersonalRepository.findById(idAulaPersonal)).thenReturn(Optional.empty());

        AulaPersonalNaoEncontradaException ex = assertThrows(AulaPersonalNaoEncontradaException.class,
                () -> service.deletarAula(idAulaPersonal));
        assertEquals(String.format(MensagensDeErroAulaPersonal.AULA_NAO_ENCONTRADA, idAulaPersonal), ex.getMessage());
    }
}
