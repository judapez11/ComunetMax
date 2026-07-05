package com.comunetmax.ms_cobertura.dto;

import com.comunetmax.ms_cobertura.model.TipoTecnologia;
import lombok.Data;

@Data
public class CoberturaDTO {
    private Long id;
    private TipoTecnologia tecnologia;
    private Long municipioId;
    private String nombreMunicipio;
    private String departamentoMunicipio;
}