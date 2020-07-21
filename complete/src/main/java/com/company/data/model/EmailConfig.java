package com.company.data.model;

import java.util.Properties;

public class EmailConfig {

    private String userName;
    private String userPass;

    private Properties mailProperties = new Properties();

    public EmailConfig() {
        this.userName = "my.watchlist.app@gmail.com";
        this.userPass = "sgaetdldsdpiqyyi";
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailProperties.put("mail.smtp.port", "465");
        mailProperties.put("mail.smtp.ssl.enable", "true");
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
