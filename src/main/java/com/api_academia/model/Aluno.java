package com.api_academia.model;

import com.api_academia.dto.AlunoDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String email;
    private String telefone;
    private String endereco;
    private String cep;
    private String peso;
    private String altura;
    private String dataCadastro;
    private Boolean cadastroAtivo;

    public Aluno() {}

    public Aluno(String nome, String cpf, String dataNascimento, String email, String telefone, String endereco, String cep, String peso, String altura, String dataCadastro) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cep = cep;
        this.peso = peso;
        this.altura = altura;
        this.dataCadastro = dataCadastro;
        this.cadastroAtivo = true;
    }

    public Aluno(AlunoDTO dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.email = dados.email();
        this.cep = dados.cep();
        this.telefone = dados.telefone();
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getCadastroAtivo() {
        return cadastroAtivo;
    }

    public void setCadastroAtivo(Boolean cadastroAtivo) {
        this.cadastroAtivo = cadastroAtivo;
    }
}
