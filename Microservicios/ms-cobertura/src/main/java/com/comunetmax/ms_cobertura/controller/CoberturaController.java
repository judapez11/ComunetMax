package com.comunetmax.ms_cobertura.controller;

import com.comunetmax.ms_cobertura.dto.CoberturaDTO;
import com.comunetmax.ms_cobertura.dto.MunicipioDTO;
import com.comunetmax.ms_cobertura.model.TipoTecnologia;
import com.comunetmax.ms_cobertura.service.CoberturaService;
import com.comunetmax.ms_cobertura.service.MunicipioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cobertura")
@RequiredArgsConstructor
public class CoberturaController {

    private final CoberturaService coberturaService;
    private final MunicipioService municipioService;

    // 1. Catálogo de municipios (Usando Service + DTO)
    @GetMapping("/municipios")
    public List<MunicipioDTO> listarMunicipios() {
        return municipioService.listarTodos();
    }

    // 2. Lista de coberturas (DTO aplanado con MapStruct)
    @GetMapping({"", "/"})
    public List<CoberturaDTO> listarCoberturas() {
        return coberturaService.listarTodas();
    }

    // 3. Metodo Puente para MS-PLANES (Ahora usando DTO de forma limpia)
    @GetMapping("/municipios/{id}")
    public ResponseEntity<CoberturaDTO> obtenerMunicipioConTecnologia(@PathVariable Long id) {
        // El service se encarga de buscar y el mapper de convertirlo a JSON
        return ResponseEntity.ok(coberturaService.obtenerPorMunicipioId(id));
    }

    // 4. Asignar o Actualizar Tecnología
    @PostMapping("/asignar/{municipioId}")
    public ResponseEntity<CoberturaDTO> asignar(@PathVariable Long municipioId, @RequestParam TipoTecnologia tecnologia) {
        return ResponseEntity.ok(coberturaService.asignarTecnologia(municipioId, tecnologia));
    }

    // 5. Eliminar cobertura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coberturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}