package com.api_academia.model;

import com.api_academia.dto.ProfessorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "professores")
@Getter
@Setter
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

    public Professor() {}

    public Professor(ProfessorDTO dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.cpf = dados.cpf();
        this.telefone = dados.telefone();
        this.cref = dados.cref();
        this.endereco = new Endereco(dados.endereco());
        this.especializacao = dados.especializacao();
        this.cadastroAtivo = true;
    }
}
