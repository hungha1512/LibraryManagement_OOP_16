package com.hunghq.librarymanagement.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service class for sending emails within the library management system.
 * Provides functionality to configure SMTP properties and send emails with specified content.
 */
public class EmailService {

    /**
     * Sends an email with the given recipient, subject, and body content.
     * Configures SMTP properties for Gmail and authenticates with application credentials.
     *
     * @param to      the recipient's email address
     * @param subject the subject line of the email
     * @param body    the content of the email body
     * @throws MessagingException if there is an error in the email sending process
     */
    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");

        String username = "librarymanagementuet@gmail.com";
        String password = "ngkq wctn kyer zegh";

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = prepareMessage(session, username, to, subject, body);

        Transport.send(message);
    }

    /**
     * Prepares an email message with the specified parameters.
     *
     * @param session the mail session object configured with SMTP properties
     * @param from    the sender's email address
     * @param to      the recipient's email address
     * @param subject the subject line of the email
     * @param body    the content of the email body
     * @return a Message object ready for sending, or null if an exception occurs
     */
    private static Message prepareMessage(Session session, String from, String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
