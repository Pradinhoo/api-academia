package com.api_academia.service;

import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.Endereco;
import com.api_academia.repository.AlunoRepository;
import jakarta.persistence.EntityExistsException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public AlunoDTO cadastrarAluno(AlunoDTO dados) {
        if (alunoRepository.findByCpf(dados.cpf()).isPresent()) {
            throw new EntityExistsException("O aluno já foi cadastrado anteriormente");}

        Aluno aluno = new Aluno(dados);
        alunoRepository.save(aluno);
        return new AlunoDTO(aluno);
    }

    public List<AlunoDTO> listarTodosOsAlunoAtivos() {
        return alunoRepository.findAllByCadastroAtivoTrue()
                .stream()
                .map(AlunoDTO::new)
                .toList();
    }

    public AlunoDTO atualizarDadosAluno(Long idAluno, AtualizaAlunoDTO dados) {
        return alunoRepository.findById(idAluno)
                .map(aluno -> {
                    if (dados.nome() != null) aluno.setNome(dados.nome());
                    if (dados.email() != null) aluno.setEmail(dados.email());
                    if (dados.telefone() != null) aluno.setTelefone(dados.telefone());

                    alunoRepository.save(aluno);
                    return new AlunoDTO(aluno);})
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno"));
    }

    public AlunoDTO atualizarEnderecoAluno(Long idAluno, AtualizaEnderecoDTO dados) {
        return alunoRepository.findById(idAluno)
                .map(aluno -> {
                    Endereco endereco = aluno.getEndereco();

                    if (dados.logradouro() != null) endereco.setLogradouro(dados.logradouro());
                    if (dados.numero() != null) endereco.setNumero(dados.numero());
                    if (dados.complemento() != null) endereco.setComplemento(dados.complemento());
                    if (dados.cidade() != null) endereco.setCidade(dados.cidade());
                    if (dados.estado() != null) endereco.setEstado(dados.estado());
                    if (dados.cep() != null) endereco.setCep(dados.cep());

                    aluno.setEndereco(endereco);

                    alunoRepository.save(aluno);
                    return new AlunoDTO(aluno);})
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno!"));
    }

    public void desativarAluno(Long idAluno) {
        Aluno aluno = alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar aluno!"));

        if(!aluno.getCadastroAtivo()) {
            throw new IllegalStateException("Não é possível desativar um aluno desativado");
        }

        aluno.setCadastroAtivo(false);
        alunoRepository.save(aluno);
    }

    public AlunoDTO localizarAlunoPorID(Long idAluno) {
        return alunoRepository.findAllById(idAluno)
                .map(AlunoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado!"));
    }
}
