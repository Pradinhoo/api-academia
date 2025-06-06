package com.api_academia.controller;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.service.CheckInAlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/check-in-aluno")
@PreAuthorize("hasAnyRole('ADMIN', 'ALUNO')")
@RequiredArgsConstructor
public class CheckInAlunoController {

    private final CheckInAlunoService checkInAlunoService;

    @PostMapping
    public ResponseEntity<String> realizarCheckInAluno(@RequestBody @Valid CheckInAlunoDTO dados) {
        checkInAlunoService.realizarCheckIn(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body("Check-In realizado com sucesso");
    }

    @GetMapping("/{id}/frequencia")
    public ResponseEntity<List<FrequenciaAlunoDTO>> frequenciaDoUltimoMes(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(checkInAlunoService.frequenciaCheckIn(id));
    }
}
