package com.api_academia.service;

import com.api_academia.dto.AtualizaEnderecoDTO;
import com.api_academia.dto.AtualizaProfessorDTO;
import com.api_academia.dto.ProfessorDTO;

import java.util.List;

public interface ProfessorService {

    ProfessorDTO cadastrarProfessor(ProfessorDTO dados);
    List<ProfessorDTO> listarProfessoresAtivos();
    ProfessorDTO atualizarDadosProfessor(Long idProfessor, AtualizaProfessorDTO dados);
    ProfessorDTO atualizarEnderecoProfessor(Long idProfessor, AtualizaEnderecoDTO dados);
    void desativarProfessor(Long idProfessor);
    void ativarProfessor(Long idProfessor);
    ProfessorDTO buscarProfessorPorId(Long idProfessor);
}
