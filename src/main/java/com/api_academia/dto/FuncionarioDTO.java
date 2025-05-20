package com.api_academia.dto;

import com.api_academia.model.Funcionario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FuncionarioDTO(

        @NotBlank (message = "Esse campo é obrigatório")
        String nome,

        @NotBlank (message = "Esse campo é obrigatório")
        String cpf,

        @NotBlank (message = "Esse campo é obrigatório")
        @Email
        String email,

        @NotBlank (message = "Esse campo é obrigatório")
        String telefone,

        @NotBlank (message = "Esse campo é obrigatório")
        @Valid
        EnderecoDTO endereco) {

        public FuncionarioDTO(Funcionario dados) {
                this(dados.getNome(), dados.getCpf(), dados.getEmail(), dados.getTelefone(), new EnderecoDTO(dados.getEndereco()));
        }
}
