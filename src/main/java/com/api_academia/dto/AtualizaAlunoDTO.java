package com.api_academia.dto;

import com.api_academia.model.Aluno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record AtualizaAlunoDTO(

        String nome,

        @Email
        String email,

        String telefone,

        @Pattern(regexp = "\\d{8}", message = "Este campo deve conter exatamente 8 dígitos")
        String cep) {

    public AtualizaAlunoDTO(Aluno aluno) {
        this(aluno.getNome(), aluno.getEmail(), aluno.getTelefone(), aluno.getCep());
    }
}

