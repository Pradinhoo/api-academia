package com.api_academia;

import com.api_academia.dto.EnderecoDTO;
import com.api_academia.dto.ProgressoDTO;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.exception.aluno.MensagensDeErroAluno;
import com.api_academia.model.Aluno;
import com.api_academia.model.Endereco;
import com.api_academia.model.Progresso;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.ProgressoRepository;
import com.api_academia.service.impl.ProgressoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressoServiceTest {

    @InjectMocks
    private ProgressoServiceImpl service;
    @Mock
    private ProgressoRepository progressoRepository;
    @Mock
    private AlunoRepository alunoRepository;

    private Long id;
    private Endereco endereco;
    private EnderecoDTO enderecoDTO;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        id = 1L;
        endereco = new Endereco("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        enderecoDTO = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        aluno = new Aluno("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
    }


    @Test
    void deveCadastrarProgressoAlunoComSucesso() {

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L);

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        service.cadastrarProgressoAluno(id, progressoDTO);

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(progressoRepository, times(1)).save(any(Progresso.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoAoCadastrarProgresso() {

        ProgressoDTO progressoDTO = new ProgressoDTO(100.00, 1.90, 1L);

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class,
                () -> service.cadastrarProgressoAluno(id, progressoDTO));
        assertEquals(String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, id), ex.getMessage());

        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(progressoRepository, never()).save(any(Progresso.class));
    }

    @Test
    void deveListarProgressoAlunoComSucessoQuandoHouverProgresso() {

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        List<ProgressoDTO> resultado = service.listarProgressoAluno(id);

        assertNotNull(resultado);
        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
    }

    @Test
    void deveListarProgressoAlunoComSucessoQuandoNaoHouverProgresso() {

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        List<ProgressoDTO> resultado = service.listarProgressoAluno(id);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
    }
}
