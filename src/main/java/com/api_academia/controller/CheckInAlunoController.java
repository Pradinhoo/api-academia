package com.api_academia.controller;

import com.api_academia.dto.CheckInAlunoDTO;
import com.api_academia.dto.FrequenciaAlunoDTO;
import com.api_academia.service.CheckInAlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/check-in-aluno")
public class CheckInAlunoController {

    @Autowired
    private CheckInAlunoService checkInAlunoService;

    @PostMapping
    public ResponseEntity<String> realizarCheckInAluno(@RequestBody @Valid CheckInAlunoDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInAlunoService.realizarCheckIn(dados));
    }

    @GetMapping("/frequencia/{idAluno}")
    public ResponseEntity<List<FrequenciaAlunoDTO>> frequenciaDoUltimoMes(@PathVariable Long idAluno) {
        return ResponseEntity.status(HttpStatus.OK).body(checkInAlunoService.frequenciaCheckIn(idAluno));
    }
}
