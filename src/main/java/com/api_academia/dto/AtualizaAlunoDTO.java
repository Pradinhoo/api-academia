package com.api_academia.dto;

import com.api_academia.model.Aluno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record AtualizaAlunoDTO(

        String nome,

        @Email
        String email,

        String telefone) {
}

