package com.api_academia.validations;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.repository.AulaPersonalRepository;

public class ValidarConflitoAluno implements ValidarCadastroAula {

    private final AulaPersonalRepository aulaPersonalRepository;

    public ValidarConflitoAluno(AulaPersonalRepository aulaPersonalRepository) {
        this.aulaPersonalRepository = aulaPersonalRepository;
    }

    public void validar(AulaPersonalDTO dados) {
        var conflitoAluno = aulaPersonalRepository.verificarConflitoAluno(
                dados.idAluno(), dados.dataHoraAula(), dados.dataHoraAulaFim());

        if(!conflitoAluno.isEmpty()) {
            throw new IllegalStateException("Conflito de horário: ALUNO já possui uma aula marcada nesse horário!");
        }
    }
}
