package com.api_academia.service;


import com.api_academia.dto.AlunoDTO;
import com.api_academia.dto.AtualizaAlunoDTO;
import com.api_academia.dto.AtualizaEnderecoDTO;

import java.util.List;

public interface AlunoService {
    AlunoDTO cadastrarAluno(AlunoDTO dados);
    List<AlunoDTO> listarTodosOsAlunoAtivos();
    AlunoDTO atualizarDadosAluno(Long idAluno, AtualizaAlunoDTO dados);
    AlunoDTO atualizarEnderecoAluno(Long idAluno, AtualizaEnderecoDTO dados);
    void desativarAluno(Long idAluno);
    void ativarAluno(Long idAluno);
    AlunoDTO localizarAlunoPorId(Long idAluno);
}
