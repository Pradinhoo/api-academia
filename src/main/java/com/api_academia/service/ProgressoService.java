package com.api_academia.service;

import com.api_academia.dto.ProgressoDTO;

import java.util.List;

public interface ProgressoService {
    void cadastrarProgressoAluno(Long id, ProgressoDTO dados);
    List<ProgressoDTO> listarProgressoAluno(Long id);
}
