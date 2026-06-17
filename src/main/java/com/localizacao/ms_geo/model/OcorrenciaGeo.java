package com.localizacao.ms_geo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ocorrencia_geo")
@NoArgsConstructor
public class OcorrenciaGeo {

    @Id
    private Long id;

    private String categoria;
    private String status;
    private Integer quantidadeDenuncias;

    // Campo geográfico do PostGIS — armazena latitude/longitude
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point localizacao;

    private LocalDateTime dataCriacao;

    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
}
