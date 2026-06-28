package com.localizacao.ms_geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Endereço resolvido a partir de coordenadas geográficas via OpenStreetMap Nominatim")
public class ReverseGeoResponseDTO {

    @Schema(description = "Nome da cidade", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado (UF)", example = "São Paulo")
    private String estado;

    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @Schema(description = "Nome da rua ou avenida", example = "Avenida Paulista")
    private String rua;

    @Schema(description = "Número do imóvel", example = "1578")
    private String numero;

    @Schema(description = "CEP", example = "01310-200")
    private String cep;
}
