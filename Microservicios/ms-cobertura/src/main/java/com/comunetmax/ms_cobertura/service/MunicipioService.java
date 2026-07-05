package com.comunetmax.ms_cobertura.service;

import com.comunetmax.ms_cobertura.dto.MunicipioDTO;
import com.comunetmax.ms_cobertura.mapper.CoberturaMapper;
import com.comunetmax.ms_cobertura.model.Municipio;
import com.comunetmax.ms_cobertura.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipioService {
    private final MunicipioRepository municipioRepository;
    private final CoberturaMapper mapper;

    public List<MunicipioDTO> listarTodos() {
        return municipioRepository.findAll().stream()
                .map(mapper::toMunicipioDto)
                .collect(Collectors.toList());
    }

    public MunicipioDTO crear(String nombre, String departamento) {
        Municipio m = new Municipio();
        m.setNombre(nombre);
        m.setDepartamento(departamento);
        return mapper.toMunicipioDto(municipioRepository.save(m));
    }
}