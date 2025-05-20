package com.api_academia.repository;

import com.api_academia.dto.ProgressoDTO;
import com.api_academia.model.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgressoRepository extends JpaRepository<Progresso, Long> {

    @Query("SELECT p FROM Progresso p WHERE p.aluno.id = :idAluno")
    List<ProgressoDTO> listaProgressoAluno(@Param("idAluno") Long id);
}
