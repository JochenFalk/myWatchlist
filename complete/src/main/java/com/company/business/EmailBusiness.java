package com.company.business;

import com.company.data.EmailQueries;
import com.company.data.UserQueries;
import com.company.data.model.*;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailBusiness {

    private static final EmailConfig emailConfig = new EmailConfig();
    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private static final String BASE_URL = applicationConfig.getAppHost() + applicationConfig.getAppPort();
    private static final String CONFIRMATION_URL = "/verifyRegistration?token=";

    public static void sendConfirmationEmail(User user) {

        String type = "Confirmation";
        String emailAddress = user.getEmailAddress();
        String token = user.getToken();
        String subject = "Registration Confirmation";
        String body = "Thank you for registering. Please click on the below link to activate your account.";
        String htmlBody = "<strong>" + body + "</strong><br><br><a href=" + BASE_URL + CONFIRMATION_URL + token + ">Click here to activate your account</a>";

        Email email = new Email(type, emailAddress, subject, htmlBody);

        sendEmail(email, user);
    }

    public static void resendConfirmationEmail(User user) {

        user.setToken();
        user.setTokenExpiryDate();

        String type = "Confirmation";
        String emailAddress = user.getEmailAddress();
        String token = user.getToken();
        String subject = "Validation link request";
        String body = "This email was sent because a new account validation link was requested. Please click on the below link to activate your account.";
        String htmlBody = "<strong>" + body + "</strong><br><br><a href=" + BASE_URL + CONFIRMATION_URL + token + ">Click here to activate your account</a>";

        Email email = new Email(type, emailAddress, subject, htmlBody);

        int validationAttempts = user.getValidationAttempts();
        validationAttempts++;
        user.setValidationAttempts(validationAttempts);
        UserQueries.updateUser(user);

        sendEmail(email, user);
    }

    private static void sendEmail(Email email, User user) {

        Session session = Session.getDefaultInstance(emailConfig.getMailProperties(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getUserName(), emailConfig.getUserPass());
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("do-not-reply"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.getRecipient()));
            message.setSubject(email.getSubject());
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            message.setText(email.getBody());
            message.setContent(email.getBody(), "text/html");
            Transport.send(message);

            insertEmail(email, user);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void insertEmail(Email email, User user) {

        // TODO: known issue: replace dirty while loop with interface callback

        long userId;

        String userName = user.getName();
        User newUser = UserQueries.getUserByName(userName);

        if (newUser != null) {

            userId = newUser.getId();

            while (userId == 0) {
                User checkUser = UserQueries.getUserByName(userName);
                if (checkUser != null) {
                    newUser.setId(checkUser.getId());
                    userId = checkUser.getId();
                }
            }
        }

        EmailQueries.insertEmail(email, newUser);
    }
}
