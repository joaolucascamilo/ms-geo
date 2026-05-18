package com.localizacao.ms_geo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapaCalorDTO {

    private String categoria;
    private Long totalOcorrencias;
    private Double centroLongitude;
    private Double centroLatitude;
}
