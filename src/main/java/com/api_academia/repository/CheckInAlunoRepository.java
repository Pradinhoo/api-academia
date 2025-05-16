package com.api_academia.repository;

import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.model.CheckInAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckInAlunoRepository extends JpaRepository<CheckInAluno, Long> {

    @Query("SELECT new com.api_academia.dto.FrequenciaAlunoDTO(c.dataHoraCheckIn) " +
            "FROM CheckInAluno c " +
            "WHERE c.aluno.id = :idAluno " +
            "AND c.dataHoraCheckIn BETWEEN :dataHoraCheckInFinal AND :dataHoraCheckInInicial")
    List<FrequenciaAlunoDTO> listaFrequenciaUltimos30Dias(@Param("idAluno") Long idAluno,
                                                          @Param("dataHoraCheckInFinal") LocalDateTime dataHoraCheckInFinal,
                                                          @Param("dataHoraCheckInInicial") LocalDateTime dataHoraCheckInInicial);

    @Query("SELECT c FROM CheckInAluno c WHERE c.aluno.id = :idAluno AND dataHoraCheckIn BETWEEN :inicioDoDia AND :finalDoDia")
    List<CheckInAluno> verificaCheckInNoMesmoDia(@Param("idAluno") Long idAluno,
                                                 @Param("inicioDoDia") LocalDateTime inicioDoDia,
                                                 @Param("finalDoDia") LocalDateTime finalDoDia);
}
