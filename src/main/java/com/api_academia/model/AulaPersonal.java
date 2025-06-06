package com.api_academia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "aulas")
@Getter @NoArgsConstructor
public class AulaPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAluno", nullable = false)
    private Aluno aluno;
    @JoinColumn(name = "idProfessor", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Professor professor;
    private LocalDateTime dataHoraAula;
    private LocalDateTime dataHoraAulaFim;

    public AulaPersonal(Aluno aluno, Professor professor, LocalDateTime dataHoraAula) {
        this.aluno = aluno;
        this.professor = professor;
        this.dataHoraAula = dataHoraAula;
        this.dataHoraAulaFim = this.dataHoraAula.plusMinutes(59);
    }
}
