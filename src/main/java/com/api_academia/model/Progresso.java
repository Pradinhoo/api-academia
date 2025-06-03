package com.api_academia.model;

import com.api_academia.dto.ProgressoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "progresso")
@Getter
@Setter
public class Progresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double peso;
    @Column(nullable = false)
    private Double altura;
    private Double valorIMC;
    @Enumerated(EnumType.STRING)
    private ClassificacaoIMC classificacaoIMC;
    private LocalDate dataRegistro;
    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    public Progresso() {}

    public Progresso(ProgressoDTO dados, Aluno aluno) {
        this.peso = dados.peso();
        this.altura = dados.altura();
        this.aluno = aluno;
        this.dataRegistro = LocalDate.now();
    }

    public Progresso(Double peso, Double altura, Double valorIMC, ClassificacaoIMC classificacaoIMC, Aluno aluno) {
        this.peso = peso;
        this.altura = altura;
        this.valorIMC = valorIMC;
        this.classificacaoIMC = classificacaoIMC;
        this.dataRegistro = LocalDate.now();
        this.aluno = aluno;
    }
}
