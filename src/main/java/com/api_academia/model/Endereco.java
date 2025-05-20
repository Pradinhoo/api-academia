package com.api_academia.model;

import com.api_academia.dto.EnderecoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Embeddable
@Getter
@Setter
public class Endereco {

    private String logradouro;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco() {}

    public Endereco(EnderecoDTO dados) {
        this.logradouro = dados.logradouro();
        this.numero = dados.numero();
        this.complemento = dados.complemento();
        this.cidade = dados.cidade();
        this.estado = dados.estado();
        this.cep = dados.cep();
    }
}
