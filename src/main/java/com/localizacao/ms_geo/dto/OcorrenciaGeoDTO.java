package com.localizacao.ms_geo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OcorrenciaGeoDTO {
    private Long id;
    private String categoria;
    private String status;
    private Integer quantidadeDenuncias;
    private LocalDateTime dataCriacao;

    // Caminho 1 — endereço textual
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;

    // Caminho 2 — coordenadas diretas (GPS ou clique no mapa)
    private Double latitude;
    private Double longitude;
}
