package com.api_academia.model;

import com.api_academia.dto.AtualizaEnderecoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Embeddable
@Getter @NoArgsConstructor @AllArgsConstructor
public class Endereco {

    private String logradouro;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;

    public void atualizarEndereco(AtualizaEnderecoDTO dados) {
        if (dados.logradouro() != null) this.logradouro = dados.logradouro();
        if (dados.numero() != null) this.numero = dados.numero();
        if (dados.complemento() != null) this.complemento = dados.complemento();
        if (dados.cidade() != null) this.cidade = dados.cidade();
        if (dados.estado() != null) this.estado = dados.estado();
        if (dados.cep() != null) this.cep = dados.cep();
    }
}
