package com.api_academia;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.CheckInAluno;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.CheckInAlunoRepository;
import com.api_academia.service.CheckInAlunoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CheckInAlunoServiceTest {

    @InjectMocks
    private CheckInAlunoService service;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private CheckInAlunoRepository checkInAlunoRepository;

    @Test
    void deveRealizarCheckInComSucesso() {
        String cpf = "123.456.789-00";
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        LocalDateTime finalDoDia = LocalDate.now().atTime(LocalTime.MAX);
        CheckInAlunoDTO dto = new CheckInAlunoDTO(cpf);

        Aluno aluno = new Aluno();
        aluno.setId(1L);

        when(alunoRepository.localizarAlunoPorCpf(cpf)).thenReturn(aluno);
        when(checkInAlunoRepository.verificaCheckInNoMesmoDia(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        String resultado = service.realizarCheckIn(dto);

        assertEquals("Check-In realizado com sucesso", resultado);
        verify(checkInAlunoRepository, times(1)).save(any(CheckInAluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoParaCheckIn() {
        String cpf = "123.456.789-00";
        CheckInAlunoDTO dto = new CheckInAlunoDTO(cpf);

        when(alunoRepository.localizarAlunoPorCpf(cpf)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.realizarCheckIn(dto));

        assertEquals("Aluno não encontrado ou sem cadastro ativo", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoParaFrequencia() {
        Long id = 1L;

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.frequenciaCheckIn(id));

        assertEquals("Aluno não encontrado ou não possui cadastro ativo", ex.getMessage());
    }

    @Test
    void deveRetornarUmaListaComAFrequenciaDos30DiasAtras() {
        Long id = 1L;
        LocalDateTime dataHoraCheckInFinal = LocalDateTime.now();
        LocalDateTime dataHoraCheckInInicial = dataHoraCheckInFinal.minusDays(30);
        FrequenciaAlunoDTO dto = new FrequenciaAlunoDTO(dataHoraCheckInFinal);

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(new Aluno()));
        when(checkInAlunoRepository.listaFrequenciaUltimos30Dias(eq(id), any(), any()))
                .thenReturn(List.of(dto));

        List<FrequenciaAlunoDTO> resultado = service.frequenciaCheckIn(id);

        assertNotNull(resultado);
        assertEquals(dataHoraCheckInFinal, resultado.get(0).dataHoraCheckIn());
    }

    @Test
    void deveLancarExcecaoQuandoAlunoJaFezCheckInNoMesmoDia() {
        String cpf = "111.222.333-44";
        Long id = 1L;
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        LocalDateTime finalDoDia = LocalDate.now().atTime(LocalTime.MAX);
        CheckInAlunoDTO dto = new CheckInAlunoDTO(cpf);

        Aluno aluno = new Aluno();
        aluno.setId(id);

        when(alunoRepository.localizarAlunoPorCpf(cpf)).thenReturn(aluno);
        when(checkInAlunoRepository.verificaCheckInNoMesmoDia(eq(id), any(), any()))
                .thenReturn(List.of(new CheckInAluno()));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                service.realizarCheckIn(dto));

        assertEquals("Somente um Check-In pode ser realizado por dia", ex.getMessage());
    }
}
