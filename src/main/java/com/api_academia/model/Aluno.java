package com.api_academia.model;

import com.api_academia.dto.AlunoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "alunos")
@Getter
@Setter
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

    public Aluno() {}

    public Aluno(AlunoDTO dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.dataNascimento = dados.dataNascimento();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.endereco = new Endereco(dados.endereco());
        this.dataCadastro = dados.dataCadastro();
        this.cadastroAtivo = true;
    }
}
