package com.api_academia.model;

import com.api_academia.dto.AulaPersonalDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aulas")
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

    public AulaPersonal(AulaPersonalDTO dados, Aluno aluno, Professor professor) {
        this.aluno = aluno;
        this.professor = professor;
        this.dataHoraAula = dados.dataHoraAula();
    }

    public AulaPersonal() {
    }

    public AulaPersonal(Aluno aluno, Professor professor, LocalDateTime dataHoraAula) {
        this.aluno = aluno;
        this.professor = professor;
        this.dataHoraAula = dataHoraAula;
        this.dataHoraAulaFim = this.dataHoraAula.plusMinutes(59);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public LocalDateTime getDataHoraAula() {
        return dataHoraAula;
    }

    public void setDataHoraAula(LocalDateTime dataHoraAula) {
        this.dataHoraAula = dataHoraAula;
    }

    public LocalDateTime getDataHoraAulaFim() {
        return dataHoraAulaFim;
    }

    public void setDataHoraAulaFim(LocalDateTime dataHoraAulaFim) {
        this.dataHoraAulaFim = dataHoraAulaFim;
    }
}
