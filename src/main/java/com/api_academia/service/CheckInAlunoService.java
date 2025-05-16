package com.api_academia.service;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.model.Aluno;
import com.api_academia.model.CheckInAluno;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.CheckInAlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CheckInAlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CheckInAlunoRepository checkInAlunoRepository;

    public CheckInAlunoService(AlunoRepository alunoRepository, CheckInAlunoRepository checkInAlunoRepository) {
    }

    public String realizarCheckIn (CheckInAlunoDTO dados) {
        Aluno aluno = alunoRepository.localizarAlunoPorCpf(dados.cpf());
        if (aluno == null) {
            throw new EntityNotFoundException("Aluno não encontrado ou sem cadastro ativo");
        }

        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        LocalDateTime finalDoDia = LocalDate.now().atTime(LocalTime.MAX);

        List<CheckInAluno> listaDeCheckIn = checkInAlunoRepository.verificaCheckInNoMesmoDia(aluno.getId(), inicioDoDia, finalDoDia);
        if (listaDeCheckIn.isEmpty()) {
            CheckInAluno checkInAluno = new CheckInAluno(aluno);
            checkInAlunoRepository.save(checkInAluno);
            return  "Check-In realizado com sucesso";
        }
        throw new IllegalStateException("Somente um Check-In pode ser realizado por dia");
    }

    public List<FrequenciaAlunoDTO> frequenciaCheckIn(Long idAluno) {

        var aluno = alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado ou não possui cadastro ativo"));

        LocalDateTime dataHoraCheckInFinal = LocalDateTime.now();
        LocalDateTime dataHoraCheckInInicial = dataHoraCheckInFinal.minusDays(30);

        return checkInAlunoRepository.listaFrequenciaUltimos30Dias(idAluno, dataHoraCheckInFinal, dataHoraCheckInInicial);
    }
}
