package com.api_academia;

import com.api_academia.dto.*;
import com.api_academia.model.Especializacao;
import com.api_academia.model.Professor;
import com.api_academia.repository.ProfessorRepository;
import com.api_academia.service.ProfessorService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    @InjectMocks
    private ProfessorService service;
    @Mock
    private ProfessorRepository professorRepository;

    @Test
    void deveCadastrarProfessorComSucesso() {
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);
        professor.setId(1L);

        when(professorRepository.findByCpf(professor.getCpf())).thenReturn(Optional.empty());
        when(professorRepository.save(any(Professor.class))).thenReturn(professor); // ← ESSENCIAL


        ProfessorDTO resultado = service.cadastrarProfessor(professorDTO);

        assertEquals(professorDTO.cpf(), resultado.cpf());

        verify(professorRepository, times(1)).findByCpf(professor.getCpf());
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorJaEstiverCadastrado() {
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);

        when(professorRepository.findByCpf(professor.getCpf())).thenReturn(Optional.of(professor));

        EntityExistsException ex = assertThrows(EntityExistsException.class,
                () -> service.cadastrarProfessor(professorDTO));
        assertEquals("O professor já foi cadastrado anteriormente", ex.getMessage());
        verify(professorRepository, times(1)).findByCpf(professor.getCpf());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveListarProfessoresAtivosComSucessoQuandoExistirProfessores() {
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);

        when(professorRepository.findAllByCadastroAtivoTrue()).thenReturn(List.of(professor));

        List<ProfessorDTO> resultado = service.listarProfessoresAtivos();

        assertNotNull(resultado);
        verify(professorRepository, times(1)).findAllByCadastroAtivoTrue();
    }

    @Test
    void deveTrazerUmaListaVaziaQuandoNaoExistirProfessoresAtivos() {
        Professor professor = new Professor();

        when(professorRepository.findAllByCadastroAtivoTrue()).thenReturn(Collections.emptyList());

        List<ProfessorDTO> resultado = service.listarProfessoresAtivos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(professorRepository, times(1)).findAllByCadastroAtivoTrue();
    }

    @Test
    void deveAtualizarOsDadosDosProfessoresComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);

        AtualizaProfessorDTO atualizaProfessorDTO = new AtualizaProfessorDTO("nomeAtualizado", "email.atualizado@email.com", "telefoneAtualizado");

        when(professorRepository.buscaProfessorAtivoPorId(id)).thenReturn(Optional.of(professor));

        ProfessorDTO resultado = service.atualizarDadosProfessor(id, atualizaProfessorDTO);

        assertNotNull(resultado);
        assertEquals(atualizaProfessorDTO.nome(), resultado.nome());
        assertEquals(atualizaProfessorDTO.email(), resultado.email());
        assertEquals(atualizaProfessorDTO.telefone(), resultado.telefone());

        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoForEncontradoAoAtualizarOsDados() {
        Long id = 1L;
        AtualizaProfessorDTO atualizaProfessorDTO = new AtualizaProfessorDTO("nomeAtualizado", "email.atualizado@email.com", "telefoneAtualizado");

        when(professorRepository.buscaProfessorAtivoPorId(id)).thenReturn((Optional.empty()));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.atualizarDadosProfessor(id, atualizaProfessorDTO));
        assertEquals("Erro ao tentar localizar professor!", ex.getMessage());

        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveAtualizarOEnderecoDoProfessorComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);

        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

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
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoAtualizarEndereco() {
        Long id = 1L;

        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.atualizarEnderecoProfessor(id, atualizaEnderecoDTO));
        assertEquals("Erro ao tentar localizar professor!", ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveDesativarProfessorComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);

        when(professorRepository.buscaProfessorAtivoPorId(id)).thenReturn(Optional.of(professor));

        service.desativarProfessor(id);

        assertFalse(professor.getCadastroAtivo());
        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoDesativar() {
        Long id = 1L;

        when(professorRepository.buscaProfessorAtivoPorId(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.desativarProfessor(id));
        assertEquals("Erro ao tentar localizar professor!", ex.getMessage());

        verify(professorRepository, times(1)).buscaProfessorAtivoPorId(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveAtivarProfessorComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);
        professor.setCadastroAtivo(false);

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));


        service.ativarProfessor(id);

        assertTrue(professor.getCadastroAtivo());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoAtivar() {
        Long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.ativarProfessor(id));
        assertEquals("Erro ao tentar localizar professor!", ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveBuscarProfessorPeloIdComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        ProfessorDTO professorDTO = new ProfessorDTO("nomeProfessor", "email@email.com", "111.222.333-44", "11900000000","123456-G", endereco, Especializacao.MUSCULACAO);
        Professor professor = new Professor(professorDTO);

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        ProfessorDTO resultado = service.buscarProfessorPorId(id);

        assertEquals(professor.getNome(), resultado.nome());
        assertEquals(professor.getEmail(), resultado.email());
        assertEquals(professor.getCpf(), resultado.cpf());
        assertEquals(professor.getTelefone(), resultado.telefone());
        assertEquals(professor.getCref(), resultado.cref());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoEncontradoAoBsucarProId() {
        Long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.buscarProfessorPorId(id));
        assertEquals("Professor não encontrado", ex.getMessage());

        verify(professorRepository, times(1)).findById(id);
        verify(professorRepository, never()).save(any(Professor.class));
    }
}
