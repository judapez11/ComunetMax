package com.comunetmax.ms_contacto.service;

import com.comunetmax.ms_contacto.dto.ContactoDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("")
    private String emailContacto;

    @Value("")
    private String emailPlanes;

    @Value("")
    private String emailPqr;

    @Override
    public void enviarCorreoContacto(ContactoDTO contacto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Preparar datos para la plantilla HTML
            Context context = new Context();
            context.setVariable("nombre", contacto.getNombre());
            context.setVariable("apellidos", contacto.getApellidos());
            context.setVariable("correo", contacto.getCorreoElectronico());
            context.setVariable("telefono", contacto.getTelefono());
            context.setVariable("mensaje", contacto.getMensaje());
            context.setVariable("tipo", contacto.getTipo());
            context.setVariable("asunto", contacto.getAsunto());
            context.setVariable("plan", contacto.getPlan());
            context.setVariable("municipio", contacto.getMunicipio());

            // Subject y Destinatario según el tipo de formulario
            String tipo = contacto.getTipo() == null ? "GENERAL" : contacto.getTipo().toUpperCase();
            String subject;
            String destinatario;

            switch (tipo) {
                case "PLANES":
                    subject = "Interes en Planes - " + (contacto.getPlan() != null ? contacto.getPlan() : "") +
                            (contacto.getMunicipio() != null ? " | " + contacto.getMunicipio() : "") +
                            " | " + contacto.getNombre();
                    destinatario = emailPlanes;
                    break;
                case "PQR":
                    subject = "PQR: " + (contacto.getAsunto() != null ? contacto.getAsunto() : "Sin asunto") +
                            " - " + contacto.getNombre();
                    destinatario = emailPqr;
                    break;
                default:
                    subject = "Nuevo Contacto de: " + contacto.getNombre();
                    destinatario = emailContacto;
                    break;
            }

            // "email-template" debe coincidir con el nombre de tu archivo .html en resources/templates
            String htmlContent = templateEngine.process("email-template", context);

            helper.setTo(destinatario);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // El 'true' activa el renderizado de HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al procesar el envío de correo", e);
        }
    }
}
