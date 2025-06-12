package com.api_academia.model;

import com.api_academia.dto.AtualizaProfessorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professores")
@Getter @NoArgsConstructor
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String cref;
    @Embedded
    private Endereco endereco;
    @Enumerated(EnumType.STRING)
    private Especializacao especializacao;
    private Boolean cadastroAtivo;
    @OneToOne
    private Usuario usuario;

    public Professor(String nome, String email, String cpf, String telefone, String cref, Endereco endereco, Especializacao especializacao) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cref = cref;
        this.endereco = endereco;
        this.especializacao = especializacao;
        this.cadastroAtivo = true;
    }

    public void atualizarDadosProfessor(AtualizaProfessorDTO dados) {
        if (dados.nome() != null) this.nome = dados.nome();
        if (dados.email() != null) this.email = dados.email();
        if (dados.telefone() != null) this.telefone = dados.telefone();
    }

    public void desativarCadastroProfessor() {
        this.cadastroAtivo = false;
    }

    public void ativarCadastroProfessor() {
        this.cadastroAtivo = true;
    }

    public void cadastrarIdProfessor(Long idProfessor) {this.id = idProfessor;}
}
