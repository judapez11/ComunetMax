package com.comunetmax.ms_cobertura.mapper;

import com.comunetmax.ms_cobertura.dto.CoberturaDTO;
import com.comunetmax.ms_cobertura.dto.MunicipioDTO;
import com.comunetmax.ms_cobertura.model.Cobertura;
import com.comunetmax.ms_cobertura.model.Municipio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoberturaMapper {

    @Mapping(source = "municipio.id", target = "municipioId")
    @Mapping(source = "municipio.nombre", target = "nombreMunicipio")
    @Mapping(source = "municipio.departamento", target = "departamentoMunicipio")
    CoberturaDTO toCoberturaDto(Cobertura cobertura);

    @Mapping(target = "municipio", ignore = true)
    Cobertura toCoberturaEntity(CoberturaDTO dto);

    MunicipioDTO toMunicipioDto(Municipio municipio);
    Municipio toMunicipioEntity(MunicipioDTO dto);
}