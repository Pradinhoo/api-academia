package com.api_academia;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.dto.EnderecoDTO;
import com.api_academia.exception.aluno.AlunoJaAtivoException;
import com.api_academia.exception.aluno.AlunoJaCadastradoException;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.exception.aluno.MensagensDeErroAluno;
import com.api_academia.mapper.AlunoMapper;
import com.api_academia.model.Aluno;
import com.api_academia.model.Endereco;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.service.impl.AlunoServiceImpl;
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
class AlunoServiceTest {

    @InjectMocks
    private AlunoServiceImpl service;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private AlunoMapper alunoMapper;

    private Endereco endereco;
    private EnderecoDTO enderecoDTO;
    private Aluno aluno;
    private AlunoDTO alunoDTO;


    @BeforeEach
    void setUp() {
        endereco = new Endereco("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        enderecoDTO = new EnderecoDTO("logradouro", "numero", "complemento", "cidade", "estado", "00000000");
        aluno = new Aluno("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", endereco, "11/22/3333");
        alunoDTO = new AlunoDTO("Teste Unitário", "11122233344", "11/22/3333", "email@email.com","1192233-4455", enderecoDTO, "11/22/3333");
    }

    @Test
    void deveCadastrarAlunoComSucesso() {

        when(alunoRepository.findByCpf(alunoDTO.cpf())).thenReturn(Optional.empty());
        when(alunoMapper.toEntity(alunoDTO)).thenReturn(aluno);
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        when(alunoMapper.toDto(aluno)).thenReturn(alunoDTO);

        AlunoDTO resultado = service.cadastrarAluno(alunoDTO);

        assertNotNull(resultado);
        assertEquals(alunoDTO.cpf(), resultado.cpf());
        assertEquals(alunoDTO.nome(), resultado.nome());

        verify(alunoRepository, times(1)).findByCpf(alunoDTO.cpf());
        verify(alunoMapper, times(1)).toEntity(alunoDTO);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
        verify(alunoMapper, times(1)).toDto(aluno);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoJaEstiverCadastrado() {

        when(alunoRepository.findByCpf(alunoDTO.cpf())).thenReturn(Optional.of(aluno));

        AlunoJaCadastradoException ex = assertThrows(AlunoJaCadastradoException.class,
                () -> service.cadastrarAluno(alunoDTO));
        String mensagemEsperada = String.format(MensagensDeErroAluno.ALUNO_JA_CADASTRADO_ANTERIORMENTE, aluno.getCpf());

        assertEquals(mensagemEsperada, ex.getMessage());
        verify(alunoRepository, times(1)).findByCpf(alunoDTO.cpf());
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveListarTodosOsAlunoAtivosComSucessoQuandoExistirAlunos() {

        when(alunoRepository.findAllByCadastroAtivoTrue()).thenReturn(List.of(aluno));
        when(alunoMapper.toDto(aluno)).thenReturn(alunoDTO);

        List<AlunoDTO> resultado = service.listarTodosOsAlunoAtivos();

        assertNotNull(resultado);
        assertEquals(alunoDTO.cpf(), resultado.get(0).cpf());

        verify(alunoRepository, times(1)).findAllByCadastroAtivoTrue();
        verify(alunoMapper, times(1)).toDto(aluno);
    }

    @Test
    void deveTrazerUmaListaVaziaQuandoNaoExistirAlunosAtivados() {
        when(alunoRepository.findAllByCadastroAtivoTrue()).thenReturn(Collections.emptyList());

        List<AlunoDTO> resultado = service.listarTodosOsAlunoAtivos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(alunoRepository, times(1)).findAllByCadastroAtivoTrue();
    }

    @Test
    void deveAtualizarOsDadosDosAlunosComSucesso() {
        Long id = 1L;
        AtualizaAlunoDTO atualizaAlunoDTO = new AtualizaAlunoDTO("Aluno Atualizado", "email@email.com", "00000000000");
        AlunoDTO alunoAtualizadoDTO = new AlunoDTO("Aluno Atualizado", "11122233344", "11/22/3333", "email@email.com","00000000000", enderecoDTO, "11/22/3333");

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));
        when(alunoMapper.toDto(any(Aluno.class))).thenReturn(alunoAtualizadoDTO);

        AlunoDTO resultado = service.atualizarDadosAluno(id, atualizaAlunoDTO);

        assertNotNull(resultado);
        assertEquals(atualizaAlunoDTO.nome(), resultado.nome());
        assertEquals(atualizaAlunoDTO.email(), resultado.email());
        assertEquals(atualizaAlunoDTO.telefone(), resultado.telefone());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
        verify(alunoMapper, times(1)).toDto(aluno);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoAoAtualizarDados() {
        Long id = 1L;
        AtualizaAlunoDTO atualizaAlunoDTO = new AtualizaAlunoDTO("Aluno Atualizado", "email@email.com", "00000000000");

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class,
            () -> service.atualizarDadosAluno(id, atualizaAlunoDTO));
        String mensagemEsperada = String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, id);

        assertEquals(mensagemEsperada, ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveAtualizarEnderecoDoAlunoComSucesso() {
        Long idAluno = 1L;
        AtualizaEnderecoDTO dadosAtualizados = new AtualizaEnderecoDTO(
                "Rua Nova", "123", "Apto 5", "Nova Cidade", "SP", "12345678");
        Endereco enderecoAntigo = new Endereco(
                "Rua Velha", "456", "Casa", "Cidade Velha", "RJ", "87654321");
        Aluno alunoAntigo = new Aluno(
                "Aluno Teste", "11122233344", "01/01/2000", "aluno@email.com",
                "11999998888", enderecoAntigo, "01/01/2024");
        AlunoDTO alunoDTOAtualizado = new AlunoDTO(
                "Aluno Teste", "11122233344", "01/01/2000", "aluno@email.com",
                "11999998888", new EnderecoDTO("Rua Nova", "123", "Apto 5", "Nova Cidade", "SP", "12345678"),
                "01/01/2024");

        when(alunoRepository.findById(idAluno)).thenReturn(Optional.of(alunoAntigo));
        when(alunoMapper.toDto(alunoAntigo)).thenReturn(alunoDTOAtualizado);

        AlunoDTO resultado = service.atualizarEnderecoAluno(idAluno, dadosAtualizados);

        assertNotNull(resultado);
        assertEquals(dadosAtualizados.logradouro(), resultado.endereco().logradouro());
        assertEquals(dadosAtualizados.numero(), resultado.endereco().numero());
        assertEquals(dadosAtualizados.complemento(), resultado.endereco().complemento());
        assertEquals(dadosAtualizados.cidade(), resultado.endereco().cidade());
        assertEquals(dadosAtualizados.estado(), resultado.endereco().estado());
        assertEquals(dadosAtualizados.cep(), resultado.endereco().cep());

        verify(alunoRepository, times(1)).findById(idAluno);
        verify(alunoRepository, times(1)).save(alunoAntigo);
        verify(alunoMapper, times(1)).toDto(alunoAntigo);
    }


    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoAoAtualizarEndereco() {
        Long id = 1L;
        AtualizaEnderecoDTO atualizaEnderecoDTO = new AtualizaEnderecoDTO("logradouroAtualizado", "numeroAtualizado", "complementoAtualizado", "cidadeAtualizada", "estadoAtualizado", "00000000");

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class,
                () -> service.atualizarEnderecoAluno(id, atualizaEnderecoDTO));
        String mensagemEsperada = String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, id);

        assertEquals(mensagemEsperada, ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveDesativarAlunoComSucesso() {
        Long id = 1L;
        aluno.cadastrarIdAluno(id);

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        service.desativarAluno(aluno.getId());

        assertFalse(aluno.getCadastroAtivo());
        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoAoAtivar() {
        Long id = 1L;

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class,
                () -> service.ativarAluno(id));
        String mensagemEsperada = String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, id);

        assertEquals(mensagemEsperada, ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveAtivarAlunoComSucesso() {
        Long id = 1L;

        aluno.desativarCadastroAluno();

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        service.ativarAluno(id);

        assertTrue(aluno.getCadastroAtivo());
        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoJaTiverCadastroAtivo() {
        Long id = 1L;
        aluno.ativarCadastroAluno();

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        AlunoJaAtivoException ex = assertThrows(AlunoJaAtivoException.class,
                () -> service.ativarAluno(id));
        String mensagemEsperada = String.format(MensagensDeErroAluno.ALUNO_JA_ATIVADO, id);

        assertEquals(mensagemEsperada, ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveLocalizarAlunoPorIdComSucesso() {
        Long id = 1L;

        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));
        when(alunoMapper.toDto(aluno)).thenReturn(alunoDTO);

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
        verify(alunoMapper, times(1)).toDto(aluno);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontradoaoLocalizarPorId() {
        Long id = 1L;

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        AlunoNaoEncontradoException ex = assertThrows(AlunoNaoEncontradoException.class,
                () -> service.localizarAlunoPorId(id));
        String mensagemEsperada = String.format(MensagensDeErroAluno.ALUNO_NAO_ENCONTRADO, id);

        assertEquals(mensagemEsperada, ex.getMessage());

        verify(alunoRepository, times(1)).findById(id);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
}
