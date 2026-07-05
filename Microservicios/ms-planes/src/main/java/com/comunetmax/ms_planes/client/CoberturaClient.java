package com.comunetmax.ms_planes.client;

import com.comunetmax.ms_planes.dto.CoberturaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "ms-cobertura", url = "http://ms-cobertura:8080")
public interface CoberturaClient {

    // Cambiamos MunicipioDTO por CoberturaDTO
    @GetMapping("/api/cobertura/municipios/{id}")
    CoberturaDTO obtenerCoberturaPorMunicipio(@PathVariable("id") Long id);
}