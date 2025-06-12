package com.api_academia.model;

import com.api_academia.dto.AtualizaAlunoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "alunos")
@Getter @NoArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String email;
    private String telefone;
    @Embedded
    private Endereco endereco;
    private String dataCadastro;
    private Boolean cadastroAtivo;
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Progresso> progresso;
    @OneToOne
    private Usuario usuario;

    public Aluno(String nome, String cpf, String dataNascimento, String email, String telefone, Endereco endereco, String dataCadastro) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.dataCadastro = dataCadastro;
        this.cadastroAtivo = true;
    }

    public void atualizaDadosAluno(AtualizaAlunoDTO dados) {
        if (dados.nome() != null) this.nome = dados.nome();
        if (dados.email() != null) this.email = dados.email();
        if (dados.telefone() != null) this.telefone = dados.telefone();
    }

    public void ativarCadastroAluno() {
        this.cadastroAtivo = true;
    }

    public void desativarCadastroAluno() {
        this.cadastroAtivo = false;
    }

    // Apenas para testes
    public void cadastrarIdAluno(Long idAluno) {this.id = idAluno;}
}
