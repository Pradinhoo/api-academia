package com.api_academia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
@Getter @NoArgsConstructor
public class CheckInAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;
    private LocalDateTime dataHoraCheckIn;

    public CheckInAluno(Aluno aluno, LocalDateTime dataHoraCheckIn) {
        this.aluno = aluno;
        this.dataHoraCheckIn = dataHoraCheckIn;
    }
}


