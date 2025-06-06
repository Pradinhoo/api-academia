package com.api_academia.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record AlunoDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        String nome,

        @NotBlank (message = "Esse campo é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "Este campo deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank (message = "Esse campo é obrigatório")
        String dataNascimento,

        @NotBlank (message = "Esse campo é obrigatório")
        @Email
        String email,

        @NotBlank (message = "Esse campo é obrigatório")
        String telefone,

        @NotNull (message = "Esse campo é obrigatório")
        @Valid
        EnderecoDTO endereco,

        @NotBlank (message = "Esse campo é obrigatório")
        String dataCadastro) {
}
