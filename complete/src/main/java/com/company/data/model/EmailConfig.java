package com.company.data.model;

import java.util.Properties;

public class EmailConfig {

    private String userName;
    private String userPass;

    private Properties mailProperties = new Properties();

    public EmailConfig() {
        this.userName = "9158ddcfaae709";
        this.userPass = "a4ce05f9430fd5";
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.host", "smtp.mailtrap.io");
        mailProperties.put("mail.smtp.port", "2525");
        mailProperties.put("mail.smtp.starttls.enable", "true");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public Properties getMailProperties() {
        return mailProperties;
    }

    public void setMailProperties(Properties mailProperties) {
        this.mailProperties = mailProperties;
    }
}
