package com.localizacao.ms_geo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class OcorrenciaGeo {

    @jakarta.persistence.Id
    @Id
    private UUID id;

    private String categoria;
    private String status;
    private Integer quantidadeDenuncias;

    // Campo geográfico do PostGIS — armazena latitude/longitude
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point localizacao;

    private LocalDateTime dataCriacao;
}
