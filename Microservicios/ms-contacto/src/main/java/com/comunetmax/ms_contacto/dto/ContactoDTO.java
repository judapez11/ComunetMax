package com.comunetmax.ms_contacto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para capturar los datos del formulario de contacto.
 * Cumple con la norma de no usar entidades de base de datos.
 */
@Data
public class ContactoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String apellidos;

    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un formato de correo válido")
    private String correoElectronico;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 10, max = 500, message = "El mensaje debe tener entre 10 y 500 caracteres")
    private String mensaje;

    private String tipo;

    private String asunto;

    private String plan;

    private String municipio;
}