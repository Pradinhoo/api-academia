package com.api_academia;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.exception.aluno.MensagensDeErroAluno;
import com.api_academia.exception.checkin.AlunoNaoEncontradoPeloCpfException;
import com.api_academia.exception.checkin.ErroAoRealizarCheckInException;
import com.api_academia.exception.checkin.MensagensDeErroCheckIn;
import com.api_academia.model.Aluno;
import com.api_academia.model.CheckInAluno;
import com.api_academia.model.Endereco;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.CheckInAlunoRepository;
import com.api_academia.service.impl.CheckInAlunoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInAlunoServiceTest {

    @InjectMocks
    private CheckInAlunoServiceImpl service;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private CheckInAlunoRepository checkInAlunoRepository;

    private Long id;
    private String cpf;
    private CheckInAlunoDTO checkInAlunoDTO;
    private Endereco endereco;
    private Aluno aluno;
    private FrequenciaAlunoDTO frequenciaAlunoDTO;


    @BeforeEach
    void setUp() {
        id = 1L;
        cpf = "12345678900";
        checkInAlunoDTO = new CheckInAlunoDTO(cpf);
        endereco = new Endereco("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        aluno = new Aluno("Nome Aluno", "12345678900", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        frequenciaAlunoDTO = new FrequenciaAlunoDTO(LocalDateTime.now());
    }

    @Test
    void deveRealizarCheckInComSucesso() {
        aluno.cadastrarIdAluno(id);

        when(alunoRepository.localizarAlunoPorCpf(cpf)).thenReturn(Optional.of(aluno));
        when(checkInAlunoRepository.verificaCheckInNoMesmoDia(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        service.realizarCheckIn(checkInAlunoDTO);

        assertEquals(cpf, aluno.getCpf());

        verify(alunoRepository, times(1)).localizarAlunoPorCpf(cpf);
        verify(checkInAlunoRepository, times(1)).verificaCheckInNoMesmoDia(eq(1L), any(), any());
        verify(checkInAlunoRepository, times(1)).save(any(CheckInAluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoParaCheckIn() {
        when(alunoRepository.localizarAlunoPorCpf(cpf)).thenReturn(Optional.empty());

        AlunoNaoEncontradoPeloCpfException ex = assertThrows(AlunoNaoEncontradoPeloCpfException.class, () ->
                service.realizarCheckIn(checkInAlunoDTO));

        assertEquals(String.format(MensagensDeErroCheckIn.CPF_NAO_ENCONTRADO, cpf), ex.getMessage());

        verify(alunoRepository, times(1)).localizarAlunoPorCpf(cpf);
        verify(checkInAlunoRepository, never()).save(any(CheckInAluno.class));
    }

    @Test
    void deveRetornarUmaListaComAFrequenciaDos30DiasAtras() {

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(new Aluno()));
        when(checkInAlunoRepository.listaFrequenciaUltimos30Dias(eq(id), any(), any()))
                .thenReturn(List.of(frequenciaAlunoDTO));

        List<FrequenciaAlunoDTO> resultado = service.frequenciaCheckIn(id);

        assertNotNull(resultado);

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(checkInAlunoRepository, times(1)).listaFrequenciaUltimos30Dias(eq(id), any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoParaFrequencia() {

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class, () ->
                service.frequenciaCheckIn(id));
        assertEquals(String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, id), ex.getMessage());

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoJaFezCheckInNoMesmoDia() {
        aluno.cadastrarIdAluno(id);

        when(alunoRepository.localizarAlunoPorCpf(cpf)).thenReturn(Optional.of(aluno));
        when(checkInAlunoRepository.verificaCheckInNoMesmoDia(eq(id), any(), any()))
                .thenReturn(List.of(new CheckInAluno()));

        ErroAoRealizarCheckInException ex = assertThrows(ErroAoRealizarCheckInException.class, () ->
                service.realizarCheckIn(checkInAlunoDTO));
        assertEquals(String.format(MensagensDeErroCheckIn.ERRO_AO_REALIZAR_CHECKIN), ex.getMessage());

        verify(alunoRepository, times(1)).localizarAlunoPorCpf(cpf);
        verify(checkInAlunoRepository, times(1)).verificaCheckInNoMesmoDia(eq(id), any(), any());
    }
}
