package com.localizacao.ms_geo.repository;

import com.localizacao.ms_geo.model.OcorrenciaGeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OcorrenciaGeoRepository extends JpaRepository<OcorrenciaGeo, UUID> {

    // Busca ocorrências num raio de X metros
    @Query(value = """
        SELECT * FROM ocorrencia_geo
        WHERE ST_DWithin(
            localizacao::geography,
            ST_SetSRID(ST_Point(:lng, :lat), 4326)::geography,
            :raioMetros
        )
        """, nativeQuery = true)
    List<OcorrenciaGeo> buscarPorRaio(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("raioMetros") double raioMetros
    );

    // Agrupa ocorrências próximas (para mapa de calor)
    @Query(value = """
        SELECT categoria, COUNT(*) as total,
               ST_X(ST_Centroid(ST_Collect(localizacao))) as centroLng,
               ST_Y(ST_Centroid(ST_Collect(localizacao))) as centroLat
        FROM ocorrencia_geo
        WHERE status != 'RESOLVIDO'
        GROUP BY categoria
        """, nativeQuery = true)
    List<Object[]> agruparParaMapaDeCalor();
}
