package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.object.EmailDetails;
import com.group2.kahootclone.service.Interface.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailService implements IEmailService {
    final ExecutorService emailExecutor = Executors.newFixedThreadPool(5);

    static class SendEmailWorker implements Runnable {
        JavaMailSender javaMailSender;
        private String sender;
        MimeMessage message;

        public SendEmailWorker(JavaMailSender javaMailSender, String sender, MimeMessage message) {
            this.javaMailSender = javaMailSender;
            this.sender = sender;
            this.message = message;
        }

        @Override
        public void run() {
            // Sending the mail
            javaMailSender.send(message);
        }
    }

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleEmail(EmailDetails details) {
        try {
            // Creating a simple mail message
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage());
            MimeMailMessage mailMessage
                    = new MimeMailMessage(mimeMessageHelper);
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setSubject(details.getSubject());

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(details.getMsgBody(), "text/html");
            mp.addBodyPart(htmlPart);
            mailMessage.getMimeMessage().setContent(mp);

            emailExecutor.execute(new SendEmailWorker(javaMailSender, sender, mailMessage.getMimeMessage()));
            return "Mail Sent Successfully...";
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "Send email is failed...";
        }
    }
}
