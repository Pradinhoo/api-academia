package com.api_academia.service;

import com.api_academia.dto.AulaPersonalDTO;

import java.util.List;

public interface AulaPersonalService {

    void cadastrarAula(AulaPersonalDTO dados);
    List<AulaPersonalDTO> listarTodasAsAulasFuturas();
    List<AulaPersonalDTO> listarAulasFuturasDoAluno(Long idAluno);
    List<AulaPersonalDTO> listarAulasFuturasDoProfessor(Long idProfessor);
    void deletarAula(Long idAula);
}
