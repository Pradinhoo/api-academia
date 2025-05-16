package com.api_academia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
public class CheckInAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;
    private LocalDateTime dataHoraCheckIn;

    public CheckInAluno() {}

    public CheckInAluno(Aluno aluno) {
        this.aluno = aluno;
        this.dataHoraCheckIn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public LocalDateTime getDataHoraCheckIn() {
        return dataHoraCheckIn;
    }
}


