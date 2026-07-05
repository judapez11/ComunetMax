package com.comunetmax.ms_cobertura.service;

import com.comunetmax.ms_cobertura.model.Municipio;
import com.comunetmax.ms_cobertura.repository.MunicipioRepository;
import jakarta.annotation.PostConstruct; // Importante para que corra solo
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataSeeder {

    private final MunicipioRepository municipioRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void seed() {
        if (municipioRepository.count() == 0) {
            String url = "https://www.datos.gov.co/resource/gdxc-w37w.json?$limit=2000";
            System.out.println("Intentando cargar municipios desde la API correcta...");

            try {
                // Leemos la respuesta como una lista de mapas
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                if (response != null) {
                    for (Map<String, Object> map : response) {
                        Municipio mun = new Municipio();

                        // USAMOS LAS LLAVES REALES DE LA API
                        mun.setNombre((String) map.get("nom_mpio"));
                        mun.setDepartamento((String) map.get("dpto"));

                        municipioRepository.save(mun);
                    }
                    System.out.println("¡Éxito! Cargados " + municipioRepository.count() + " municipios.");
                }
            } catch (Exception e) {
                System.err.println("Error en la carga: " + e.getMessage());
            }
        }
    }

    private void cargarManual() {
        System.out.println("⚠Iniciando carga manual (Plan B)...");
        List<String[]> ciudades = List.of(
                new String[]{"Medellín", "Antioquia"},
                new String[]{"Bogotá", "Cundinamarca"},
                new String[]{"Cali", "Valle del Cauca"},
                new String[]{"Barranquilla", "Atlántico"},
                new String[]{"Bucaramanga", "Santander"}
        );

        for (String[] item : ciudades) {
            Municipio mun = new Municipio();
            mun.setNombre(item[0]);
            mun.setDepartamento(item[1]);
            municipioRepository.save(mun);
        }
        System.out.println("Carga manual de emergencia completada.");
    }
}