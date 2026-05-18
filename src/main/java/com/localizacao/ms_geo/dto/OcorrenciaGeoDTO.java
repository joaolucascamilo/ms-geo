package com.localizacao.ms_geo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OcorrenciaGeoDTO {

    private UUID id;

    private String categoria;
    private String status;
    private Integer quantidadeDenuncias;
    private LocalDateTime dataCriacao;
    private double longitude;
    private double latitude;
}
