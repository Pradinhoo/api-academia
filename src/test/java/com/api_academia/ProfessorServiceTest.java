package com.api_academia;

import com.api_academia.dto.*;
import com.api_academia.exception.professor.MensagensDeErroProfessor;
import com.api_academia.exception.professor.ProfessorJaCadastradoException;
import com.api_academia.exception.professor.ProfessorNaoEncontradoException;
import com.api_academia.mapper.ProfessorMapper;
import com.api_academia.model.Endereco;
import com.api_academia.model.Especializacao;
import com.api_academia.model.Professor;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.impl.ProfessorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @InjectMocks
    private ProfessorServiceImpl service;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private ProfessorMapper professorMapper;

    private EnderecoDTO enderecoDTO;
    private Endereco endereco;
    private ProfessorDTO professorDTO;
    private Professor professor;

    @BeforeEach
    void setUp() {
        endereco = new Endereco("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        enderecoDTO = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        professor = new Professor("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", enderecoDTO, Especializacao.MUSCULACAO);
    }

    @Test
    void deveCadastrarProfessorComSucesso() {

        when(professorRepository.findByCpf(professorDTO.cpf())).thenReturn(Optional.empty());
        when(professorMapper.toEntity(professorDTO)).thenReturn(professor);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);
        when(professorMapper.toDto(professor)).thenReturn(professorDTO);

        ProfessorDTO resultado = service.cadastrarProfessor(professorDTO);

        assertEquals(professorDTO.cpf(), resultado.cpf());

        verify(professorRepository, times(1)).findByCpf(professorDTO.cpf());
        verify(professorMapper, times(1)).toEntity(professorDTO);
        verify(professorRepository, times(1)).save(any(Professor.class));
        verify(professorMapper, times(1)).toDto(professor);
    }

    @Test
    void deveLancarExcecaoQuandoProfessorJaEstiverCadastrado() {

        when(professorRepository.findByCpf(professorDTO.cpf())).thenReturn(Optional.of(professorDTO));

        ProfessorJaCadastradoException ex = assertThrows(ProfessorJaCadastradoException.class,
                () -> service.cadastrarProfessor(professorDTO));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_JA_CADASTRADO), ex.getMessage());

        verify(professorRepository, times(1)).findByCpf(professorDTO.cpf());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveListarProfessoresAtivosComSucessoQuandoExistirProfessores() {

        when(professorRepository.findAllByCadastroAtivoTrue()).thenReturn(List.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDTO);

        List<ProfessorDTO> resultado = service.listarProfessoresAtivos();

        assertNotNull(resultado);

        verify(professorRepository, times(1)).findAllByCadastroAtivoTrue();
        verify(professorMapper, times(1)).toDto(professor);
    }

    @Test
    void deveTrazerUmaListaVaziaQuandoNaoExistirProfessoresAtivos() {

        when(professorRepository.findAllByCadastroAtivoTrue()).thenReturn(Collections.emptyList());

        List<ProfessorDTO> resultado = service.listarProfessoresAtivos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(professorRepository, times(1)).findAllByCadastroAtivoTrue();
    }

    @Test
    void deveAtualizarOsDadosDosProfessoresComSucesso() {
        Long id = 1L;
        AtualizaProfessorDTO atualizaProfessorDTO = new AtualizaProfessorDTO("nomeAtualizado", "email.atualizado@email.com", "telefoneAtualizado");
        ProfessorDTO professorAtualizado = new ProfessorDTO ("nomeAtualizado", "email.atualizado@email.com", "111.222.333-44", "telefoneAtualizado","123456-G", enderecoDTO, Especializacao.MUSCULACAO);

        when(professorRepository.buscaProfessorAtivoPorId(id)).thenReturn(Optional.of(professor));
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorMapper.toDto(professor)).thenReturn(professorAtualizado);

        ProfessorDTO resultado = service.atualizarDadosProfessor(id, atualizaProfessorDTO);

        assertNotNull(resultado);
        assertEquals(atualizaProfessorDTO.nome(), resultado.nome());
        assertEquals(atualizaProfessorDTO.email(), resultado.email());
        assertEquals(atualizaProfessorDTO.telefone(), resultado.telefone());

        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
        verify(professorMapper, times(1)).toDto(professor);
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoForEncontradoAoAtualizarOsDados() {
        Long id = 1L;
        AtualizaProfessorDTO atualizaProfessorDTO = new AtualizaProfessorDTO("nomeAtualizado", "email.atualizado@email.com", "telefoneAtualizado");

        when(professorRepository.buscaProfessorAtivoPorId(id)).thenReturn((Optional.empty()));

        ProfessorNaoEncontradoException ex = assertThrows(ProfessorNaoEncontradoException.class,
                () -> service.atualizarDadosProfessor(id, atualizaProfessorDTO));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, id), ex.getMessage());

        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveAtualizarOEnderecoDoProfessorComSucesso() {
        Long id = 1L;
        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");
        ProfessorDTO professorAtualizadoDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", new EnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000"), Especializacao.MUSCULACAO);

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorMapper.toDto(professor)).thenReturn(professorAtualizadoDTO);

        ProfessorDTO resultado = service.atualizarEnderecoProfessor(id, atualizaEnderecoDTO);

        assertNotNull(resultado);
        assertEquals(atualizaEnderecoDTO.logradouro(), resultado.endereco().logradouro());
        assertEquals(atualizaEnderecoDTO.numero(), resultado.endereco().numero());
        assertEquals(atualizaEnderecoDTO.complemento(), resultado.endereco().complemento());
        assertEquals(atualizaEnderecoDTO.cidade(), resultado.endereco().cidade());
        assertEquals(atualizaEnderecoDTO.estado(), resultado.endereco().estado());
        assertEquals(atualizaEnderecoDTO.cep(), resultado.endereco().cep());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
        verify(professorMapper, times(1)).toDto(professor);
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoAtualizarEndereco() {
        Long id = 1L;
        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        ProfessorNaoEncontradoException ex = assertThrows(ProfessorNaoEncontradoException.class,
                () -> service.atualizarEnderecoProfessor(id, atualizaEnderecoDTO));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, id), ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveDesativarProfessorComSucesso() {
        Long id = 1L;
        professor.cadastrarIdProfessor(id);

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        service.desativarProfessor(professor.getId());

        assertFalse(professor.getCadastroAtivo());
        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoDesativar() {
        Long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        ProfessorNaoEncontradoException ex = assertThrows(ProfessorNaoEncontradoException.class,
                () -> service.desativarProfessor(id));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, id), ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveAtivarProfessorComSucesso() {
        Long id = 1L;
        professor.cadastrarIdProfessor(id);
        professor.desativarCadastroProfessor();

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        service.ativarProfessor(professor.getId());

        assertTrue(professor.getCadastroAtivo());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoAtivar() {
        Long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        ProfessorNaoEncontradoException ex = assertThrows(ProfessorNaoEncontradoException.class,
                () -> service.ativarProfessor(id));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, id), ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveBuscarProfessorPeloIdComSucesso() {
        Long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDTO);

        ProfessorDTO resultado = service.buscarProfessorPorId(id);

        assertEquals(professorDTO.nome(), resultado.nome());
        assertEquals(professorDTO.email(), resultado.email());
        assertEquals(professorDTO.cpf(), resultado.cpf());
        assertEquals(professorDTO.telefone(), resultado.telefone());
        assertEquals(professorDTO.cref(), resultado.cref());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
        verify(professorMapper, times(1)).toDto(professor);
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoBuscarProId() {
        Long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        ProfessorNaoEncontradoException ex = assertThrows(ProfessorNaoEncontradoException.class,
                () -> service.buscarProfessorPorId(id));
        assertEquals(String.format(MensagensDeErroProfessor.PROFESSOR_NAO_ENCONTRADO, id), ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }
}
