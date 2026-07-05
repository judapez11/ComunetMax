package com.comunetmax.ms_cobertura.controller;

import com.comunetmax.ms_cobertura.dto.MunicipioDTO;
import com.comunetmax.ms_cobertura.service.MunicipioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/municipios")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioService municipioService;

    @GetMapping
    public List<MunicipioDTO> listarTodos() {
        return municipioService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<MunicipioDTO> crear(@RequestBody Map<String, String> body) {
        String nombre = body.get("nombre");
        String departamento = body.get("departamento");
        if (nombre == null || nombre.isBlank() || departamento == null || departamento.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(municipioService.crear(nombre.trim(), departamento.trim()));
    }
}