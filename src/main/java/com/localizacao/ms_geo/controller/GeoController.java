package com.localizacao.ms_geo.controller;

import com.localizacao.ms_geo.dto.EnderecoDTO;
import com.localizacao.ms_geo.dto.MapaCalorDTO;
import com.localizacao.ms_geo.dto.OcorrenciaGeoDTO;
import com.localizacao.ms_geo.dto.OcorrenciaGeoResponseDTO;
import com.localizacao.ms_geo.dto.ReverseGeoResponseDTO;
import com.localizacao.ms_geo.model.OcorrenciaGeo;
import com.localizacao.ms_geo.service.GeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
@Tag(name = "Geolocalização", description = "Endpoints para registro e consulta geoespacial de ocorrências de infraestrutura urbana")
public class GeoController {

    private final GeoService geoService;

    @Operation(
        summary = "Buscar ocorrências num raio",
        description = "Retorna todas as ocorrências ativas dentro do raio especificado a partir das coordenadas fornecidas. Utiliza cálculo geoespacial via PostGIS (ST_DWithin)."
    )
    @ApiResponse(responseCode = "200", description = "Lista de ocorrências encontradas",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = OcorrenciaGeoResponseDTO.class))))
    @GetMapping("/ocorrencias/raio")
    public ResponseEntity<List<OcorrenciaGeoResponseDTO>> buscarPorRaio(
            @Parameter(description = "Latitude do ponto central", example = "-23.5505", required = true)
            @RequestParam double lat,
            @Parameter(description = "Longitude do ponto central", example = "-46.6333", required = true)
            @RequestParam double lng,
            @Parameter(description = "Raio de busca em metros (padrão: 500)", example = "500")
            @RequestParam(defaultValue = "500") double raioMetros
    ) {
        List<OcorrenciaGeoResponseDTO> resultado = geoService
                .buscarOcorrenciasPorRaio(lat, lng, raioMetros)
                .stream()
                .map(OcorrenciaGeoResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @Operation(
        summary = "Gerar dados para mapa de calor",
        description = "Retorna ocorrências agrupadas por categoria com o centroide geográfico de cada grupo. Ocorrências com status RESOLVIDA são excluídas. Consumido pelo frontend para renderizar camadas de calor no mapa."
    )
    @ApiResponse(responseCode = "200", description = "Dados de calor agrupados por categoria",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = MapaCalorDTO.class))))
    @GetMapping("/mapa-calor")
    public ResponseEntity<List<MapaCalorDTO>> mapaDeCalor() {
        return ResponseEntity.ok(geoService.gerarMapaDeCalor());
    }

    @Operation(
        summary = "Consultar endereço por CEP",
        description = "Resolve um CEP brasileiro em dados de endereço via ViaCEP. Útil para preencher formulários de ocorrência automaticamente a partir do CEP informado pelo cidadão."
    )
    @ApiResponse(responseCode = "200", description = "Endereço correspondente ao CEP",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EnderecoDTO.class)))
    @ApiResponse(responseCode = "400", description = "CEP inválido ou não encontrado")
    @GetMapping("/cep/{cep}")
    public ResponseEntity<EnderecoDTO> buscarCep(
            @Parameter(description = "CEP no formato 00000000 (sem hífen)", example = "01310100", required = true)
            @PathVariable String cep
    ) {
        return ResponseEntity.ok(geoService.buscarEnderecoPorCep(cep));
    }

    @Operation(
        summary = "Registrar ocorrência com localização",
        description = "Persiste uma ocorrência recebida do ms-ocorrencias associando-a a coordenadas geográficas. " +
                      "Aceita dois formatos de localização: (1) endereço textual — geocodificado via Nominatim/OpenStreetMap; " +
                      "(2) coordenadas diretas — informadas pelo dispositivo GPS ou clique no mapa."
    )
    @ApiResponse(responseCode = "201", description = "Ocorrência registrada com sucesso",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = OcorrenciaGeoResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido")
    @PostMapping("/ocorrencias")
    public ResponseEntity<OcorrenciaGeoResponseDTO> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados da ocorrência. Informe (rua/bairro/cidade/estado/pais) OU (latitude/longitude).",
                required = true,
                content = @Content(schema = @Schema(implementation = OcorrenciaGeoDTO.class)))
            @RequestBody @Valid OcorrenciaGeoDTO dto
    ) {
        OcorrenciaGeo salvo = geoService.salvarOcorrencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OcorrenciaGeoResponseDTO.fromEntity(salvo));
    }

    @Operation(
        summary = "Geocodificação reversa",
        description = "Converte coordenadas geográficas (latitude/longitude) em dados de endereço legível via OpenStreetMap Nominatim. Utilizado para exibir o endereço da localização atual do usuário."
    )
    @ApiResponse(responseCode = "200", description = "Endereço correspondente às coordenadas",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ReverseGeoResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Coordenadas inválidas ou fora do alcance do Nominatim")
    @GetMapping("/reverse")
    public ResponseEntity<ReverseGeoResponseDTO> reverseGeocode(
            @Parameter(description = "Latitude", example = "-23.5505", required = true)
            @RequestParam double lat,
            @Parameter(description = "Longitude", example = "-46.6333", required = true)
            @RequestParam double lon
    ) {
        return ResponseEntity.ok(geoService.reverseGeocode(lat, lon));
    }
}