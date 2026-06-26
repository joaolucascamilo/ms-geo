package com.localizacao.ms_geo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReverseGeoResponseDTO {
    private String cidade;
    private String estado;
    private String bairro;
    private String rua;
    private String numero;
    private String cep;
}
