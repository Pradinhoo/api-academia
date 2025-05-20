package com.api_academia.repository;

import com.api_academia.model.Aluno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Aluno findByCpf(String cpf);

    @Query("SELECT a FROM Aluno a WHERE a.id = :idAluno AND a.cadastroAtivo = true")
    Optional<Aluno> buscaAlunoAtivoPorId (@Param("idAluno") Long idAluno);

    @Query("SELECT a FROM Aluno a WHERE a.cpf = :cpf AND a.cadastroAtivo = true")
    Aluno localizarAlunoPorCpf(@Param("cpf") String cpf);

    List<Aluno> findAllByCadastroAtivoTrue();
}
