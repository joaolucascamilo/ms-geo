package com.localizacao.ms_geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Endereço retornado pela API ViaCEP a partir de um CEP brasileiro")
public class EnderecoDTO {

    @Schema(description = "CEP formatado", example = "01310-100")
    private String cep;

    @Schema(description = "Logradouro (rua/avenida)", example = "Avenida Paulista")
    private String logradouro;

    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @Schema(description = "Cidade", example = "São Paulo")
    private String localidade;

    @Schema(description = "UF (sigla do estado)", example = "SP")
    private String uf;
}
