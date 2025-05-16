package com.api_academia.dto;

import com.api_academia.model.Professor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AtualizaProfessorDTO(

        String nome,

        @Email
        String email,

        String telefone) {

    public AtualizaProfessorDTO(Professor professor) {
        this(professor.getNome(), professor.getEmail(), professor.getTelefone());
    }
}
