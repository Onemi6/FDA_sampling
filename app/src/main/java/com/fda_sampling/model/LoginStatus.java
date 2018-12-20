package com.fda_sampling.model;

public class LoginStatus {

    private String TOKEN;

    private String NAME;

    private String MESSAGE;

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getTOKEN() {
        return this.TOKEN;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getMESSAGE() {
        return this.MESSAGE;
    }
}
