package com.api_academia.model;

import com.api_academia.dto.ProfessorDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "professores")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String cref;
    private Especializacao especializacao;
    private Boolean cadastroAtivo;

    public Professor() {}

    public Professor(String nome, String email,String cpf, String telefone, String cref, Especializacao especializacao) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cref = cref;
        this.especializacao = especializacao;
        this.cadastroAtivo = true;
    }

    public Professor(ProfessorDTO dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();;
        this.cref = dados.cref();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }

    public Especializacao getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(Especializacao especializacao) {
        this.especializacao = especializacao;
    }

    public Boolean getCadastroAtivo() {
        return cadastroAtivo;
    }

    public void setCadastroAtivo(Boolean cadastroAtivo) {
        this.cadastroAtivo = cadastroAtivo;
    }
}
