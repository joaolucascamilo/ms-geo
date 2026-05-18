package com.localizacao.ms_geo.service;

import com.localizacao.ms_geo.client.ViaCEPClient;
import com.localizacao.ms_geo.dto.EnderecoDTO;
import com.localizacao.ms_geo.dto.MapaCalorDTO;
import com.localizacao.ms_geo.dto.OcorrenciaGeoDTO;
import com.localizacao.ms_geo.model.OcorrenciaGeo;
import com.localizacao.ms_geo.repository.OcorrenciaGeoRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoService {

    private final OcorrenciaGeoRepository repository;
    private final ViaCEPClient viaCEPClient;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public List<OcorrenciaGeo> buscarOcorrenciasPorRaio(double lat, double lng, double raioMetros) {
        return repository.buscarPorRaio(lat, lng, raioMetros);
    }

    public List<MapaCalorDTO> gerarMapaDeCalor() {
        return repository.agruparParaMapaDeCalor().stream()
                .map(row -> new MapaCalorDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).doubleValue(),
                        ((Number) row[3]).doubleValue()
                )).collect(Collectors.toList());
    }

    public EnderecoDTO buscarEnderecoPorCep(String cep) {
        return viaCEPClient.buscarPorCep(cep);
    }

    public OcorrenciaGeo salvarOcorrencia(OcorrenciaGeoDTO dto) {
        OcorrenciaGeo geo = new OcorrenciaGeo();
        geo.setId(dto.getId());
        geo.setCategoria(dto.getCategoria());
        geo.setStatus(dto.getStatus());
        geo.setQuantidadeDenuncias(dto.getQuantidadeDenuncias());
        geo.setDataCriacao(dto.getDataCriacao());

        // Converte lat/lng para tipo Point do PostGIS
        Point ponto = geometryFactory.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );
        geo.setLocalizacao(ponto);

        return repository.save(geo);
    }
}
