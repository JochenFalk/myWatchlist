package com.company.data.model;

public class ConfirmationEmail extends Email{

    private String type;

    public ConfirmationEmail(long id, String recipient, String subject, String body) {
        super(id, recipient, subject, body);
        this.type = getType();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
