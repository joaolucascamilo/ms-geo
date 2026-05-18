package com.localizacao.ms_geo.client;

import com.localizacao.ms_geo.dto.EnderecoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br")
public interface ViaCEPClient {

    @GetMapping("/ws/{cep}/json")
    EnderecoDTO buscarPorCep(@PathVariable("cep") String cep);
}
