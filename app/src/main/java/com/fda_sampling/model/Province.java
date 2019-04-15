package com.fda_sampling.model;

/**
 * Created by yy on 2019/4/15.
 */

public class Province {
    private int ID;

    private String NAME;

    private int ORDER_ID;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setORDER_ID(int ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public int getORDER_ID() {
        return this.ORDER_ID;
    }
}
