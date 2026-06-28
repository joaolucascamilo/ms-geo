package com.localizacao.ms_geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Dados de uma ocorrência enviados pelo ms-ocorrencias para registro geoespacial. " +
                       "Informe endereço textual (rua/bairro/cidade/estado/pais) OU coordenadas diretas (latitude/longitude).")
public class OcorrenciaGeoDTO {

    @Schema(description = "ID da ocorrência no ms-ocorrencias", example = "42")
    private Long id;

    @Schema(description = "Categoria da ocorrência", example = "BURACO_NA_VIA",
            allowableValues = {"BURACO_NA_VIA", "ILUMINACAO", "ESGOTO", "CALCADA", "SINALIZACAO", "OUTROS"})
    private String categoria;

    @Schema(description = "Status atual da ocorrência", example = "ABERTA",
            allowableValues = {"ABERTA", "EM_ANDAMENTO", "RESOLVIDA"})
    private String status;

    @Schema(description = "Quantidade de denúncias registradas para esta ocorrência", example = "3")
    private Integer quantidadeDenuncias;

    @Schema(description = "Data e hora de criação da ocorrência no ms-ocorrencias", example = "2024-08-15T10:30:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Nome da rua (usar junto com bairro/cidade/estado/pais)", example = "Av. Paulista")
    private String rua;

    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado (UF)", example = "SP")
    private String estado;

    @Schema(description = "País", example = "Brasil")
    private String pais;

    @Schema(description = "Latitude em graus decimais (WGS-84). Usar junto com longitude.", example = "-23.5630")
    private Double latitude;

    @Schema(description = "Longitude em graus decimais (WGS-84). Usar junto com latitude.", example = "-46.6543")
    private Double longitude;
}
