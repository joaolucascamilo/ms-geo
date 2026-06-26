package com.localizacao.ms_geo.client;

import com.localizacao.ms_geo.config.FeignConfig;
import com.localizacao.ms_geo.dto.NominatimResponseDTO;
import com.localizacao.ms_geo.dto.NominatimReverseResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "nominatim", url = "https://nominatim.openstreetmap.org", configuration = FeignConfig.class)
public interface NominatimClient {
    @GetMapping("/search")
    List<NominatimResponseDTO> buscarCoordenadas(
            @RequestParam("q") String endereco,
            @RequestParam("format") String formato,
            @RequestParam("limit") int limite
    );

    @GetMapping("/reverse")
    NominatimReverseResponseDTO reverseGeocode(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("format") String formato
    );
}
