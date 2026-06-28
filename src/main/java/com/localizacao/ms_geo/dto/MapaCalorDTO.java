package com.localizacao.ms_geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ponto de calor agrupado por categoria de ocorrência. " +
                       "O centroide representa o centro geográfico de todas as ocorrências ativas da categoria.")
public class MapaCalorDTO {

    @Schema(description = "Categoria das ocorrências agrupadas", example = "BURACO_NA_VIA")
    private String categoria;

    @Schema(description = "Total de ocorrências ativas nesta categoria", example = "17")
    private Long totalOcorrencias;

    @Schema(description = "Longitude do centroide geográfico do grupo (WGS-84)", example = "-46.6543")
    private Double centroLongitude;

    @Schema(description = "Latitude do centroide geográfico do grupo (WGS-84)", example = "-23.5630")
    private Double centroLatitude;
}
