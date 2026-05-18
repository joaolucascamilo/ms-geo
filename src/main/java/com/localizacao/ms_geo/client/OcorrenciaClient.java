package com.localizacao.ms_geo.client;

import com.localizacao.ms_geo.dto.OcorrenciaGeoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ms-ocorrencias", url = "${ms-ocorrencias.url}")
public interface OcorrenciaClient {

    @GetMapping("/api/ocorrencias")
    List<OcorrenciaGeoDTO> listarTodasOcorrencias();

    @GetMapping("/api/ocorrencias/{id}")
    OcorrenciaGeoDTO buscarPorId(@PathVariable UUID id);
}