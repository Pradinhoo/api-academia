package com.api_academia;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.EnderecoDTO;
import com.api_academia.dto.ProgressoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.Progresso;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.ProgressoRepository;
import com.api_academia.service.ProgressoService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProgressoServiceTest {

    @InjectMocks
    private ProgressoService service;
    @Mock
    private ProgressoRepository progressoRepository;
    @Mock
    private AlunoRepository alunoRepository;

    @Test
    void deveCadastrarProgressoAlunoComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L, LocalDate.now());

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        String mensagem = service.cadastrarProgressoAluno(id, progressoDTO);

        assertEquals("Progresso gravado com sucesso!", mensagem);

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(progressoRepository, times(1)).save(any(Progresso.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoAoCadastrarProgresso() {
        Long id = 1L;

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L, LocalDate.now());

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.cadastrarProgressoAluno(id, progressoDTO));
        assertEquals("Aluno não encontrado", ex.getMessage());

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(progressoRepository, never()).save(any(Progresso.class));
    }

    @Test
    void deveListarProgressoAlunoComSucessoQuandoHouverProgresso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L, LocalDate.now());

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        List<ProgressoDTO> resultado = service.listarProgressoAluno(id);

        assertNotNull(resultado);
        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
    }

    @Test
    void deveListarProgressoAlunoComSucessoQuandoNaoHouverProgresso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L, LocalDate.now());

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        List<ProgressoDTO> resultado = service.listarProgressoAluno(id);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoAoListarProgressoAluno() {
        Long id = 1L;

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L, LocalDate.now());

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.cadastrarProgressoAluno(id, progressoDTO));
        assertEquals("Aluno não encontrado", ex.getMessage());

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(progressoRepository, never()).save(any(Progresso.class));
    }
}
