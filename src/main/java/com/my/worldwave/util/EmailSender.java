package com.my.worldwave.util;

import com.my.worldwave.exception.auth.EmailSendingFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailSender {

    private final String SENDER;
    private final JavaMailSender javaMailSender;

    public EmailSender(@Value("${spring.mail.username}") String sender, JavaMailSender javaMailSender) {
        this.SENDER = sender;
        this.javaMailSender = javaMailSender;
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmail(String receiver, String title, String content) {
        try {
            MimeMessage email = javaMailSender.createMimeMessage();
            email.setFrom(SENDER);
            email.setRecipients(MimeMessage.RecipientType.TO, receiver);
            email.setSubject(title);
            email.setText(content);
            javaMailSender.send(email);
        } catch (Exception e) {
            throw new EmailSendingFailException();
        }
    }

}
