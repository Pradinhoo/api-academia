package com.api_academia.repository;

import com.api_academia.dto.ProfessorDTO;
import com.api_academia.model.Professor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findAllByCadastroAtivoTrue();

    @Query("SELECT p FROM Professor p WHERE p.id = :idProfessor AND p.cadastroAtivo = true")
    Optional<Professor> buscaProfessorAtivoPorId(@Param("idProfessor") Long idProfessor);

    Optional<Professor> findByCpf(String cpf);
}
