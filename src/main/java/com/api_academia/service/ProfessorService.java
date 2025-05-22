package com.api_academia.service;

import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.dto.AtualizaProfessorDTO;
import com.api_academia.dto.ProfessorDTO;
import com.api_academia.model.Endereco;
import com.api_academia.model.Professor;
import com.api_academia.repository.ProfessorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public ProfessorDTO cadastrarProfessor(ProfessorDTO dados) {
        Optional<Professor> professorCpf = professorRepository.findByCpf(dados.cpf());

        if (professorCpf.isPresent()) {
            throw new EntityExistsException("O professor já foi cadastrado anteriormente");
        }

        Professor professor = professorRepository.save(new Professor(dados));
        return new ProfessorDTO(professor);
    }

    public List<ProfessorDTO> listarProfessoresAtivos() {
        return professorRepository.findAllByCadastroAtivoTrue()
                .stream()
                .map(ProfessorDTO::new)
                .toList();
    }

    public ProfessorDTO atualizarDadosProfessor(Long idProfessor, AtualizaProfessorDTO dados) {
        return professorRepository.buscaProfessorAtivoPorId(idProfessor)
                .map(professor -> {
                    if (dados.nome() != null) professor.setNome(dados.nome());
                    if (dados.email() != null) professor.setEmail(dados.email());
                    if (dados.telefone() != null) professor.setTelefone(dados.telefone());
                    professorRepository.save(professor);
                    return new ProfessorDTO(professor);
                })
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar o professor"));
    }

    public ProfessorDTO atualizarEnderecoProfessor(Long idProfessor, AtualizaEnderecoDTO dados) {
        return professorRepository.findById(idProfessor)
                .map(professor -> {
                    Endereco endereco = professor.getEndereco();

                    if (dados.logradouro() != null) endereco.setLogradouro(dados.logradouro());
                    if (dados.numero() != null) endereco.setNumero(dados.numero());
                    if (dados.complemento() != null) endereco.setComplemento(dados.complemento());
                    if (dados.cidade() != null) endereco.setCidade(dados.cidade());
                    if (dados.estado() != null) endereco.setEstado(dados.estado());
                    if (dados.cep() != null) endereco.setCep(dados.cep());

                    professor.setEndereco(endereco);

                    professorRepository.save(professor);
                    return new ProfessorDTO(professor);
                }).orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar professor!"));
    }

    public void desativarProfessor(Long idProfessor) {
        Professor professor = professorRepository.buscaProfessorAtivoPorId(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizar professor!"));

        professor.setCadastroAtivo(false);
        professorRepository.save(professor);
    }

    public void ativarProfessor(Long idProfessor) {
        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao tentar localizae professor!"));

        if (professor.getCadastroAtivo()) {
            throw new IllegalStateException("Não é possível ativar um professor já ativo");
        }

        professor.setCadastroAtivo(true);
        professorRepository.save(professor);
    }

    public ProfessorDTO buscarProfessorPorId(Long idProfessor) {
        return professorRepository.findById(idProfessor)
                .map(ProfessorDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
    }
}
