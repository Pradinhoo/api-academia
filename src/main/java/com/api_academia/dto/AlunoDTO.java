package com.api_academia.dto;

import com.api_academia.model.Aluno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record AlunoDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        String nome,

        @NotBlank (message = "Esse campo é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "Este campo deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank (message = "Esse campo é obrigatório")
        @Email
        String email,

        @NotBlank (message = "Esse campo é obrigatório")
        String telefone,

        @NotBlank (message = "Esse campo é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "Este campo deve conter exatamente 8 dígitos")
        String cep) {

        public AlunoDTO(Aluno aluno) {
                this(aluno.getNome(), aluno.getCpf(), aluno.getEmail(), aluno.getTelefone(), aluno.getCep());
        }
}
