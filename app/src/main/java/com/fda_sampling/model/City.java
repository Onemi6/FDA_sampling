package com.fda_sampling.model;

/**
 * Created by yy on 2019/4/15.
 */

public class City {
    private int ID;

    private int PROVINCE_ID;

    private String NAME;

    private String AREACODE;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setPROVINCE_ID(int PROVINCE_ID) {
        this.PROVINCE_ID = PROVINCE_ID;
    }

    public int getPROVINCE_ID() {
        return this.PROVINCE_ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setAREACODE(String AREACODE) {
        this.AREACODE = AREACODE;
    }

    public String getAREACODE() {
        return this.AREACODE;
    }
}
