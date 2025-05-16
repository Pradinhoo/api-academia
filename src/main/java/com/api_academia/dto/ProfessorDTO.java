package com.api_academia.dto;

import com.api_academia.model.Professor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfessorDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        String nome,

        @NotBlank (message = "Esse campo é obrigatório")
        @Email
        String email,

        @NotBlank (message = "Esse campo é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "Este campo deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank (message = "Esse campo é obrigatório")
        String telefone,

        @NotBlank (message = "Esse campo é obrigatório")
        String cref) {

        public ProfessorDTO(Professor professor) {
                this(professor.getNome(), professor.getEmail(), professor.getCpf(), professor.getTelefone(), professor.getCref());
        }
}
