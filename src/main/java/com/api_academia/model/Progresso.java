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
    private LocalDate dataHoraRegistro;
    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    public Progresso() {}

    public Progresso(ProgressoDTO dados, Aluno aluno) {
        this.peso = dados.peso();
        this.altura = dados.altura();
        this.aluno = aluno;
        this.dataHoraRegistro = LocalDate.now();
    }
}
