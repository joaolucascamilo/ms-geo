package com.localizacao.ms_geo.dto;

import com.localizacao.ms_geo.model.OcorrenciaGeo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Ocorrência com coordenadas geográficas resolvidas")
public class OcorrenciaGeoResponseDTO {

    @Schema(description = "ID da ocorrência", example = "42")
    private Long id;

    @Schema(description = "Categoria da ocorrência", example = "BURACO_NA_VIA")
    private String categoria;

    @Schema(description = "Status da ocorrência", example = "ABERTA")
    private String status;

    @Schema(description = "Quantidade de denúncias", example = "3")
    private Integer quantidadeDenuncias;

    @Schema(description = "Data e hora de criação", example = "2024-08-15T10:30:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Latitude em graus decimais (WGS-84)", example = "-23.5630")
    private Double latitude;

    @Schema(description = "Longitude em graus decimais (WGS-84)", example = "-46.6543")
    private Double longitude;

    // Converte a entidade para DTO extraindo lat/lng do Point
    public static OcorrenciaGeoResponseDTO fromEntity(OcorrenciaGeo entity) {
        OcorrenciaGeoResponseDTO dto = new OcorrenciaGeoResponseDTO();
        dto.setId(entity.getId());
        dto.setCategoria(entity.getCategoria());
        dto.setStatus(entity.getStatus());
        dto.setQuantidadeDenuncias(entity.getQuantidadeDenuncias());
        dto.setDataCriacao(entity.getDataCriacao());

        if (entity.getLocalizacao() != null) {
            dto.setLatitude(entity.getLocalizacao().getY());   // Y = latitude
            dto.setLongitude(entity.getLocalizacao().getX());  // X = longitude
        }

        return dto;
    }
}
