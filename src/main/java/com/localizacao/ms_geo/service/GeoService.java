package com.localizacao.ms_geo.service;

import com.localizacao.ms_geo.client.NominatimClient;
import com.localizacao.ms_geo.client.ViaCEPClient;
import com.localizacao.ms_geo.dto.*;
import com.localizacao.ms_geo.model.OcorrenciaGeo;
import com.localizacao.ms_geo.repository.OcorrenciaGeoRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoService {

    private final OcorrenciaGeoRepository repository;
    private final ViaCEPClient viaCEPClient;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired
    private NominatimClient nominatimClient;

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
        CoordenadasDTO coords;

        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            // Caminho 2 — coordenadas GPS, usa direto
            coords = new CoordenadasDTO(dto.getLatitude(), dto.getLongitude());

        } else if (dto.getRua() != null && dto.getCidade() != null) {
            // Caminho 1 — endereço textual, geocodifica via Nominatim
            coords = geocodificar(dto.getRua(), dto.getBairro(), dto.getCidade(), dto.getEstado(), dto.getPais());

        } else {
            throw new IllegalArgumentException(
                    "Informe pelo menos o endereço (rua + cidade) ou as coordenadas (latitude + longitude)."
            );
        }

        OcorrenciaGeo geo = new OcorrenciaGeo();
        geo.setId(dto.getId());
        geo.setCategoria(dto.getCategoria());
        geo.setStatus(dto.getStatus());
        geo.setQuantidadeDenuncias(dto.getQuantidadeDenuncias());
        geo.setDataCriacao(dto.getDataCriacao());
        geo.setRua(dto.getRua());
        geo.setBairro(dto.getBairro());
        geo.setCidade(dto.getCidade());
        geo.setEstado(dto.getEstado());
        geo.setPais(dto.getPais());
        geo.setLocalizacao(
                geometryFactory.createPoint(new Coordinate(coords.getLongitude(), coords.getLatitude()))
        );

        return repository.save(geo);
    }

    public OcorrenciaGeo atualizarApoio(Long id, Integer quantidadeDenuncias) {
        OcorrenciaGeo geo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada: " + id));
        geo.setQuantidadeDenuncias(quantidadeDenuncias);
        return repository.save(geo);
    }

    public ReverseGeoResponseDTO reverseGeocode(double lat, double lon) {
        NominatimReverseResponseDTO resultado = nominatimClient.reverseGeocode(lat, lon, "json");

        if (resultado == null || resultado.getAddress() == null) {
            throw new RuntimeException("Não foi possível determinar o endereço para as coordenadas informadas.");
        }

        NominatimReverseResponseDTO.Address address = resultado.getAddress();
        return new ReverseGeoResponseDTO(
                address.getCidade(),
                address.getState(),
                address.getBairro(),
                address.getRoad(),
                address.getNumero(),
                address.getPostcode()
        );
    }

    public CoordenadasDTO geocodificar(String rua, String bairro, String cidade, String estado, String pais) {
        // Monta a query de busca
        String query = String.format("%s, %s, %s, %s, %s", rua, bairro, cidade, estado, pais);

        List<NominatimResponseDTO> resultados = nominatimClient.buscarCoordenadas(query, "json", 1);

        if (resultados == null || resultados.isEmpty()) {
            throw new RuntimeException("Endereço não encontrado: " + query);
        }

        NominatimResponseDTO resultado = resultados.get(0);
        return new CoordenadasDTO(
                Double.parseDouble(resultado.getLat()),
                Double.parseDouble(resultado.getLon())
        );
    }
}
