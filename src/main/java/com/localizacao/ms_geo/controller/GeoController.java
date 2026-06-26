package com.localizacao.ms_geo.controller;

import com.localizacao.ms_geo.dto.EnderecoDTO;
import com.localizacao.ms_geo.dto.MapaCalorDTO;
import com.localizacao.ms_geo.dto.OcorrenciaGeoDTO;
import com.localizacao.ms_geo.dto.OcorrenciaGeoResponseDTO;
import com.localizacao.ms_geo.dto.ReverseGeoResponseDTO;
import com.localizacao.ms_geo.model.OcorrenciaGeo;
import com.localizacao.ms_geo.service.GeoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
public class GeoController {

    private final GeoService geoService;

    // Ocorrências num raio (ex.: 500m ao redor do usuário)
    @GetMapping("/ocorrencias/raio")
    public ResponseEntity<List<OcorrenciaGeoResponseDTO>> buscarPorRaio(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "500") double raioMetros
    ) {
        List<OcorrenciaGeoResponseDTO> resultado = geoService
                .buscarOcorrenciasPorRaio(lat, lng, raioMetros)
                .stream()
                .map(OcorrenciaGeoResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // Dados para o mapa de calor
    @GetMapping("/mapa-calor")
    public ResponseEntity<List<MapaCalorDTO>> mapaDeCalor() {
        return ResponseEntity.ok(geoService.gerarMapaDeCalor());
    }

    // Busca endereço pelo CEP (usado no frontend)
    @GetMapping("/cep/{cep}")
    public ResponseEntity<EnderecoDTO> buscarCep(@PathVariable String cep) {
        return ResponseEntity.ok(geoService.buscarEnderecoPorCep(cep));
    }

    // Recebe uma ocorrência do ms-ocorrencias para registrar no mapa
    @PostMapping("/ocorrencias")
    public ResponseEntity<OcorrenciaGeoResponseDTO> registrar(@RequestBody @Valid OcorrenciaGeoDTO dto) {
        OcorrenciaGeo salvo = geoService.salvarOcorrencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OcorrenciaGeoResponseDTO.fromEntity(salvo));
    }

    // Converte coordenadas em cidade/estado via Nominatim
    @GetMapping("/reverse")
    public ResponseEntity<ReverseGeoResponseDTO> reverseGeocode(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return ResponseEntity.ok(geoService.reverseGeocode(lat, lon));
    }
}