package com.sp5blue.shopshare.services.mail;

import jakarta.mail.MessagingException;

public interface IMailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendHTMLMessage(String to, String subject, String htmlText) throws MessagingException;
}
