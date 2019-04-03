package com.fda_sampling.model;

/**
 * Created by yy on 2019/4/1.
 */

public class MeasureUnit {
    private int ID;

    private String TYPE;

    private String UNIT_NAME;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setUNIT_NAME(String UNIT_NAME) {
        this.UNIT_NAME = UNIT_NAME;
    }

    public String getUNIT_NAME() {
        return this.UNIT_NAME;
    }
}
