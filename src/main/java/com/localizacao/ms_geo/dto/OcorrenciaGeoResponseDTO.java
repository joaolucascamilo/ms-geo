package com.localizacao.ms_geo.dto;

import com.localizacao.ms_geo.model.OcorrenciaGeo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OcorrenciaGeoResponseDTO {

    private Long id;
    private String categoria;
    private String status;
    private Integer quantidadeDenuncias;
    private LocalDateTime dataCriacao;
    private Double latitude;
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
