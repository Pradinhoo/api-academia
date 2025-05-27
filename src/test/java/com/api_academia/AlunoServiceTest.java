package com.api_academia;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.dto.EnderecoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.Endereco;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.service.AlunoService;
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
public class AlunoServiceTest {

    @InjectMocks
    private AlunoService service;
    @Mock
    private AlunoRepository alunoRepository;

    @Test
    void deveCadastrarAlunoComSucesso() {
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);
        aluno.setId(1L);

        when(alunoRepository.findByCpf(alunoDTO.cpf())).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);


        AlunoDTO resultado = service.cadastrarAluno(alunoDTO);

        assertNotNull(resultado);
        assertEquals(alunoDTO.cpf(), resultado.cpf());
        assertEquals(alunoDTO.nome(), resultado.nome());

        verify(alunoRepository, times(1)).findByCpf(alunoDTO.cpf());
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoJaEstiverCadastrado() {
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "111.222.333-44", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);


        when(alunoRepository.findByCpf(alunoDTO.cpf())).thenReturn(Optional.of(aluno));

        EntityExistsException ex = assertThrows(EntityExistsException.class,
                () -> service.cadastrarAluno(alunoDTO));
        assertEquals("O aluno já foi cadastrado anteriormente", ex.getMessage());
        verify(alunoRepository, times(1)).findByCpf(alunoDTO.cpf());
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveListarTodosOsAlunoAtivosComSucessoQuandoExistirAlunos() {
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "111.222.333-44", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);

        when(alunoRepository.findAllByCadastroAtivoTrue()).thenReturn(List.of(aluno));

        List<AlunoDTO> resultado = service.listarTodosOsAlunoAtivos();

        assertNotNull(resultado);
        assertEquals(alunoDTO.cpf(), resultado.get(0).cpf());
        verify(alunoRepository, times(1)).findAllByCadastroAtivoTrue();
    }

    @Test
    void deveTrazerUmaListaVaziaQuandoNaoExistirAlunosAtivados() {
        Aluno aluno = new Aluno();

        when(alunoRepository.findAllByCadastroAtivoTrue()).thenReturn(Collections.emptyList());

        List<AlunoDTO> resultado = service.listarTodosOsAlunoAtivos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(alunoRepository, times(1)).findAllByCadastroAtivoTrue();
    }

    @Test
    void deveAtualizarOsDadosDosAlunosComSucesso() {
        Long id = 1L;

        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "111.222.333-44", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);

        AtualizaAlunoDTO atualizaAlunoDTO = new AtualizaAlunoDTO("Aluno Atualizado", "email@email.com", "00000000000");


        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        AlunoDTO resultado = service.atualizarDadosAluno(id, atualizaAlunoDTO);

        assertNotNull(resultado);
        assertEquals(atualizaAlunoDTO.nome(), resultado.nome());
        assertEquals(atualizaAlunoDTO.email(), resultado.email());
        assertEquals(atualizaAlunoDTO.telefone(), resultado.telefone());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoAoAtualizarDados() {
        Long id = 1L;
        AtualizaAlunoDTO atualizaAlunoDTO = new AtualizaAlunoDTO("Aluno Atualizado", "email@email.com", "00000000000");

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> service.atualizarDadosAluno(id, atualizaAlunoDTO));
        assertEquals("Erro ao tentar localizar aluno", ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveAtualizarEnderecoDoAlunoComSucesso() {
        Long id = 1L;
        EnderecoDTO enderecoDTO = new EnderecoDTO("antigoLogradouro", "antigoNumero", "antigoComplemento", "antigaCidade", "antigaEstado", "00000000");
        Endereco endereco = new Endereco(enderecoDTO);
        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");
        Aluno aluno = new Aluno();
        aluno.setEndereco(endereco);

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        AlunoDTO resultado = service.atualizarEnderecoAluno(id, atualizaEnderecoDTO);

        assertNotNull(resultado);
        assertEquals(atualizaEnderecoDTO.logradouro(), resultado.endereco().logradouro());
        assertEquals(atualizaEnderecoDTO.numero(), resultado.endereco().numero());
        assertEquals(atualizaEnderecoDTO.complemento(), resultado.endereco().complemento());
        assertEquals(atualizaEnderecoDTO.cidade(), resultado.endereco().cidade());
        assertEquals(atualizaEnderecoDTO.estado(), resultado.endereco().estado());
        assertEquals(atualizaEnderecoDTO.cep(), resultado.endereco().cep());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoAoAtualizarEndereco() {
        Long id = 1L;
        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.atualizarEnderecoAluno(id, atualizaEnderecoDTO));
        assertEquals("Erro ao tentar localizar aluno!", ex.getMessage());
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveDesativarAlunoComSucesso() {
        Long id = 1L;
        Aluno aluno = new Aluno();
        aluno.setCadastroAtivo(true);

        when(alunoRepository.buscaAlunoAtivoPorId(id)).thenReturn(Optional.of(aluno));

        service.desativarAluno(id);

        assertFalse(aluno.getCadastroAtivo());
        verify(alunoRepository, times(1)).buscaAlunoAtivoPorId(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoAoAtivar() {
        Long id = 1L;

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.ativarAluno(id));
        assertEquals("Erro ao tentar localizar aluno!", ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveAtivarAlunoComSucesso() {
        Long id = 1L;
        Aluno aluno = new Aluno();
        aluno.setCadastroAtivo(false);

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        service.ativarAluno(id);

        assertTrue(aluno.getCadastroAtivo());
        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoJaTiverCadastroAtivo() {
        Long id = 1L;
        Aluno aluno = new Aluno();
        aluno.setCadastroAtivo(true);

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.ativarAluno(id));
        assertEquals("Não é possível ativar um aluno já ativo", ex.getMessage());
        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveLocalizarAlunoPorIdComSucesso() {
        Long id = 1L;
        EnderecoDTO endereco = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        AlunoDTO alunoDTO = new AlunoDTO("Teste Unitário", "111.222.333-44", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        Aluno aluno = new Aluno(alunoDTO);

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        AlunoDTO resultado = service.localizarAlunoPorId(id);

        assertEquals(resultado.nome(), aluno.getNome());
        assertEquals(resultado.cpf(), aluno.getCpf());
        assertEquals(resultado.dataNascimento(), aluno.getDataNascimento());
        assertEquals(resultado.email(), aluno.getEmail());
        assertEquals(resultado.telefone(), aluno.getTelefone());
        assertEquals(resultado.endereco().logradouro(), aluno.getEndereco().getLogradouro());
        assertEquals(resultado.endereco().numero(), aluno.getEndereco().getNumero());
        assertEquals(resultado.endereco().complemento(), aluno.getEndereco().getComplemento());
        assertEquals(resultado.endereco().cidade(), aluno.getEndereco().getCidade());
        assertEquals(resultado.endereco().estado(), aluno.getEndereco().getEstado());
        assertEquals(resultado.endereco().cep(), aluno.getEndereco().getCep());
        assertEquals(resultado.dataCadastro(), aluno.getDataCadastro());

        verify(alunoRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoaoLocalizarPorId() {
        Long id = 1L;
        Aluno aluno = new Aluno();

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.localizarAlunoPorId(id));
        assertEquals("Aluno não encontrado!", ex.getMessage());
        verify(alunoRepository, times(1)).findById(id);
    }
}
