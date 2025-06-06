package com.api_academia.service.impl;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.exception.aluno.AlunoNaoEncontradoException;
import com.api_academia.exception.checkin.AlunoNaoEncontradoPeloCpfException;
import com.api_academia.exception.checkin.ErroAoRealizarCheckInException;
import com.api_academia.model.Aluno;
import com.api_academia.model.CheckInAluno;
import com.api_academia.repository.AlunoRepository;
import com.api_academia.repository.CheckInAlunoRepository;

import com.api_academia.service.CheckInAlunoService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInAlunoServiceImpl implements CheckInAlunoService {

    private final AlunoRepository alunoRepository;
    private final CheckInAlunoRepository checkInAlunoRepository;

    public void realizarCheckIn (CheckInAlunoDTO dados) {
        Aluno aluno = verificaAlunoPorCpf(dados.cpf());
        verificaCheckInNoMesmoDia(aluno.getId());
        checkInAlunoRepository.save(new CheckInAluno(aluno, LocalDateTime.now()));
    }

    public List<FrequenciaAlunoDTO> frequenciaCheckIn(Long idAluno) {
        verificaAlunoPorId(idAluno);

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaDiasAtras = LocalDateTime.now().minusDays(30);

        return checkInAlunoRepository.listaFrequenciaUltimos30Dias(idAluno, agora, trintaDiasAtras);
    }

    private Aluno verificaAlunoPorCpf(String cpfAluno) {
        return alunoRepository.localizarAlunoPorCpf(cpfAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoPeloCpfException(cpfAluno));
    }

    private void verificaAlunoPorId(Long idAluno) {
         alunoRepository.buscaAlunoAtivoPorId(idAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    private void verificaCheckInNoMesmoDia(Long idAluno) {

        LocalDateTime comecoDoDia = LocalDate.now().atStartOfDay();
        LocalDateTime finalDoDia = LocalDate.now().atTime(LocalTime.MAX);

        List<CheckInAluno> listaDeCheckIn = checkInAlunoRepository.verificaCheckInNoMesmoDia(idAluno, comecoDoDia, finalDoDia);

        if (!listaDeCheckIn.isEmpty()) {
            throw new ErroAoRealizarCheckInException();
        }
    }
}
