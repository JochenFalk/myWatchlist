package com.company.data.model;

import java.util.ArrayList;

public class Email {

    private long id;
    private String type;
    private String recipient;
    private String subject;
    private String body;

    private static ArrayList<Email> Emails = new ArrayList<>();

    public Email(String type, String recipient, String subject, String body) {
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public Email(long id, String type, String recipient, String subject, String body) {
        this.id = id;
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static ArrayList<Email> getEmails() {
        return Emails;
    }

    public static void setEmails(ArrayList<Email> emails) {
        Emails = emails;
    }
}
