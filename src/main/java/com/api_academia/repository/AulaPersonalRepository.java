package com.api_academia.repository;

import com.api_academia.dto.AulaPersonalDTO;
import com.api_academia.model.AulaPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AulaPersonalRepository extends JpaRepository<AulaPersonal, Long> {

    @Query("SELECT a FROM AulaPersonal a WHERE a.dataHoraAula > CURRENT_TIMESTAMP")
    List<AulaPersonalDTO> listaTodasAsAulasFuturas();

    @Query("SELECT a FROM AulaPersonal a WHERE a.professor.id = :idProfessor AND a.dataHoraAula BETWEEN :dataHoraAula AND :dataHoraAulaFim")
    List<AulaPersonalDTO> verificarConflitoProfessor(@Param("idProfessor") Long idProfessor,
                                                  @Param("dataHoraAula") LocalDateTime dataHoraAula,
                                                  @Param("dataHoraAulaFim") LocalDateTime dataHoraAulaFim);

    @Query("SELECT a FROM AulaPersonal a WHERE a.aluno.id = :idAluno AND a.dataHoraAula BETWEEN :dataHoraAula AND :dataHoraAulaFim")
    List<AulaPersonalDTO> verificarConflitoAluno(@Param("idAluno") Long idAluno,
                                                 @Param("dataHoraAula") LocalDateTime dataHoraAula,
                                                 @Param("dataHoraAulaFim") LocalDateTime dataHoraAulaFim);

    @Query("SELECT a FROM AulaPersonal a WHERE a.aluno.id = :idAluno AND a.dataHoraAula >= :agora")
    List<AulaPersonalDTO> listaAulasMarcadasAlunos(@Param("idAluno") Long idAluno,
                                                   @Param("agora") LocalDateTime agora);

    @Query("SELECT a FROM AulaPersonal a WHERE a.professor.id = :idProfessor AND a.dataHoraAula >= :agora")
    List<AulaPersonalDTO> listaAulasMarcadasProfessores(@Param("idProfessor") Long idProfessor,
                                                        @Param("agora") LocalDateTime agora);

    Optional<AulaPersonal> findById(Long idAulaPersonal);
}
