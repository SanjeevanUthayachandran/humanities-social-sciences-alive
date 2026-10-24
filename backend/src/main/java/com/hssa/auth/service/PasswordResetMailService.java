package com.hssa.auth.service;

import com.hssa.exception.EmailDeliveryException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class PasswordResetMailService {

    private final JavaMailSender mailSender;

    @Value("${application.frontend-url}")
    private String frontendUrl;

    @Value("${application.mail.from}")
    private String fromAddress;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    public void ensureConfigured() {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new EmailDeliveryException("Password reset email is temporarily unavailable. Please contact HSSA support.");
        }
    }

    public void sendPasswordReset(String recipient, String token) {
        ensureConfigured();
        String resetUrl = frontendUrl + "/reset-password?token="
                + URLEncoder.encode(token, StandardCharsets.UTF_8);
        String sender = fromAddress == null || fromAddress.isBlank() ? username : fromAddress;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject("Reset your HSSA password");
            helper.setText(buildPlainText(resetUrl), buildHtml(resetUrl));
            mailSender.send(message);
        } catch (MessagingException | MailException exception) {
            throw new EmailDeliveryException("We could not send the reset email. Please try again later.", exception);
        }
    }

    private String buildPlainText(String resetUrl) {
        return "We received a request to reset your HSSA password.\n\n"
                + "Choose a new password: " + resetUrl + "\n\n"
                + "This secure link expires in 30 minutes and can only be used once. "
                + "If you did not request this change, you can safely ignore this email.";
    }

    private String buildHtml(String resetUrl) {
        return """
                <!doctype html>
                <html lang="en">
                  <body style="margin:0;background:#f5ece3;font-family:Arial,sans-serif;color:#1c1a17;">
                    <div style="max-width:560px;margin:0 auto;padding:40px 20px;">
                      <div style="background:#fff;padding:36px;border:1px solid #e8ded3;border-radius:18px;">
                        <p style="margin:0 0 12px;color:#c2522a;font-size:12px;font-weight:700;letter-spacing:2px;">HSSA ACCOUNT SECURITY</p>
                        <h1 style="margin:0 0 18px;font-size:28px;">Reset your password</h1>
                        <p style="margin:0 0 26px;line-height:1.6;color:#5c564f;">We received a request to reset your HSSA password. Use the secure button below to choose a new password.</p>
                        <a href="%s" style="display:inline-block;background:#c2522a;color:#fff;text-decoration:none;font-weight:700;padding:14px 22px;border-radius:999px;">Choose a new password</a>
                        <p style="margin:26px 0 0;line-height:1.6;color:#7b756d;font-size:13px;">This link expires in 30 minutes and can only be used once. If you did not request this change, you can safely ignore this email.</p>
                      </div>
                    </div>
                  </body>
                </html>
                """.formatted(resetUrl);
    }
}
