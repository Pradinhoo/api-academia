package com.api_academia.validations;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.exception.aulapersonal.ConflitoProfessorException;
import com.api_academia.repository.AulaPersonalRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidarConflitoProfessor implements ValidarCadastroAula {

    private final AulaPersonalRepository aulaPersonalRepository;

    public ValidarConflitoProfessor(AulaPersonalRepository aulaPersonalRepository) {
        this.aulaPersonalRepository = aulaPersonalRepository;
    }

    public void validar(AulaPersonalDTO dados) {
        var conflitoProfessor = aulaPersonalRepository.verificarConflitoProfessor(
                dados.idProfessor(), dados.dataHoraAula(), dados.dataHoraAulaFim());

        if (!conflitoProfessor.isEmpty()) {
            throw new ConflitoProfessorException();
        }
    }
}
