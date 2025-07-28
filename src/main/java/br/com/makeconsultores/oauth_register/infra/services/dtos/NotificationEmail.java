package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record NotificationEmail(
        String to,
        String from,
        String html,
        String subject,
        String base64Attachment,
        String base64AttachmentName,
        String message
) {
}
