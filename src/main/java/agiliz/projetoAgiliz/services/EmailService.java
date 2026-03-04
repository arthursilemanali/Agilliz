package agiliz.projetoAgiliz.services;

import freemarker.template.Configuration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Configuration fmConfig;

    @Value("${spring.mail.username}")
    private String remetente;

    @Async
    public void enviarEmail(String to, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String htmlContent = "<!DOCTYPE html>" +
                "<html lang='pt'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Redefinição de Senha</title>" +
                "    <style>" +
                "        .container { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9; }" +
                "        .header { background-color: #F67366; padding: 10px; text-align: center; color: white; border-top-left-radius: 10px; border-top-right-radius: 10px; }" +
                "        .header h1 { margin: 0; font-size: 24px; }" +
                "        .logo { text-align: center; margin: 20px 0; }" +
                "        .content { margin: 20px 0; }" +
                "        .content p { font-size: 16px; color: #333; }" +
                "        .reset-button { display: block; text-align: center; margin: 30px 0; }" +
                "        .reset-button a { background-color: #F67366; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 18px; }" +
                "        .reset-button a:hover { background-color: #e45c4e; }" +
                "        .footer { text-align: center; font-size: 12px; color: #777; margin-top: 30px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>Solicitação de Redefinição de Senha</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Olá,</p>" +
                "            <p>Recebemos uma solicitação para redefinir sua senha. Clique no botão abaixo para redefini-la.</p>" +
                "            <p>Se você não solicitou essa alteração, por favor, ignore este e-mail.</p>" +
                "            <div class='reset-button'>" +
                "                <a href='" + resetLink + "'>Redefinir Senha</a>" +
                "            </div>" +
                "            <p>Se o botão não funcionar, copie e cole o link abaixo no seu navegador:</p>" +
                "            <p>" + resetLink + "</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>&copy; 2024 Agiliz. Todos os direitos reservados.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        helper.setTo(to);
        helper.setSubject("Solicitação de Redefinição de Senha");
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public String getConteudoTemplate(Map<String,Object> modelo) {
        var conteudo = new StringBuilder();

        try {
            var templateFormatado = FreeMarkerTemplateUtils
                    .processTemplateIntoString(
                            fmConfig.getTemplate("email-recuperacao-senha.flth"), modelo
                    );

            conteudo.append(templateFormatado);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return conteudo.toString();
    }
}
