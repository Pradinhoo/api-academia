package com.api_academia.dto;

import com.api_academia.model.TipoUsuario;
import com.api_academia.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(

        @NotBlank
        String login,
        @NotNull
        TipoUsuario tipoUsuario) {

    public UsuarioDTO(Usuario dados) {
        this(dados.getLogin(), dados.getTipoUsuario());
    }
}
