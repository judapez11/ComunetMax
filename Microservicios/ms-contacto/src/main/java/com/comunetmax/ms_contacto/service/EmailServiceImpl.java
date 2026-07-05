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

    @Value("${EMAIL_TO:perez@gmail.com}")
    private String emailTo;

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

            // "email-template" debe coincidir con el nombre de tu archivo .html en resources/templates
            String htmlContent = templateEngine.process("email-template", context);

            helper.setTo(emailTo);
            helper.setSubject("Nuevo Contacto de: " + contacto.getNombre());
            helper.setText(htmlContent, true); // El 'true' activa el renderizado de HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al procesar el envío de correo", e);
        }
    }
}