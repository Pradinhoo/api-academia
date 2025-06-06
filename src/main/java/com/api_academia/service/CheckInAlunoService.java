package com.api_academia.service;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;

import java.util.List;

public interface CheckInAlunoService {
    void realizarCheckIn(CheckInAlunoDTO dados);
    List<FrequenciaAlunoDTO> frequenciaCheckIn(Long idAluno);
}
