package com.fda_sampling.model;

/**
 * Created by yy on 2019/4/15.
 */

public class District {

    private int ID;

    private int CITY_ID;

    private String NAME;

    private String POSTCODE;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setCITY_ID(int CITY_ID) {
        this.CITY_ID = CITY_ID;
    }

    public int getCITY_ID() {
        return this.CITY_ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setPOSTCODE(String POSTCODE) {
        this.POSTCODE = POSTCODE;
    }

    public String getPOSTCODE() {
        return this.POSTCODE;
    }
}
