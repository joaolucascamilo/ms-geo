package com.localizacao.ms_geo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NominatimResponseDTO {

    private String lat;
    private String lon;

    @JsonProperty("display_name")
    private String enderecoCompleto;

}
