package com.company.business;

import com.company.data.ApplicationConfig;
import com.company.data.EmailConfig;
import com.company.data.User;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailBusiness {

    private static final EmailConfig emailConfig = new EmailConfig();
    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private static final String BASE_URL = applicationConfig.getHost() + applicationConfig.getPort();

    public static void sendConfirmationEmail(User user) {

        String userEmail = user.getUserEmail();
        String userToken = user.getVerificationEmail().getUserToken();

        Session session = Session.getDefaultInstance(emailConfig.getMailProperties(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getUserName(), emailConfig.getUserPass());
            }
        });

        try {
            String subject = "Registration Confirmation";
            String body = "Thank you for registering. Please click on the below link to activate your account.";
            String confirmUrl = "/confirmRegistration?token=" + userToken;
            String htmlBody = "<strong>" + body + "</strong><br><br><a href=" + BASE_URL + confirmUrl + ">Click here to activate your account</a>";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("random@adress.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(userEmail));
            message.setSubject(subject);
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            message.setText(htmlBody);
            message.setContent(htmlBody, "text/html");
            Transport.send(message);

            System.out.println("Email sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
