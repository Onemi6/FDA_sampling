package com.fda_sampling.model;

public class LoginStatus {

    private String TOKEN;

    private String NAME;

    private String MESSAGE;

    private String NO;

    public LoginStatus(String TOKEN, String NAME, String MESSAGE, String NO) {
        super();
        this.TOKEN = TOKEN;
        this.NAME = NAME;
        this.MESSAGE = MESSAGE;
        this.NO = NO;
    }

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

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getNO() {
        return this.NO;
    }
}
