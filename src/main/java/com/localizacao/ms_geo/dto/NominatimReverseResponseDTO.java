package com.localizacao.ms_geo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NominatimReverseResponseDTO {

    private String lat;
    private String lon;

    @JsonProperty("display_name")
    private String enderecoCompleto;

    private Address address;

    @Data
    public static class Address {
        private String city;
        private String town;
        private String village;
        private String municipality;
        private String road;
        private String suburb;
        private String neighbourhood;
        private String state;
        private String country;

        @JsonProperty("house_number")
        private String numero;

        private String postcode;

        @JsonProperty("country_code")
        private String codigoPais;

        public String getCidade() {
            if (city != null) return city;
            if (municipality != null) return municipality;
            if (town != null) return town;
            return village;
        }

        public String getBairro() {
            if (suburb != null) return suburb;
            return neighbourhood;
        }
    }
}
