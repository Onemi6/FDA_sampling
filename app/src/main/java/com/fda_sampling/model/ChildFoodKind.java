package com.fda_sampling.model;

/**
 * Created by yy on 2019/2/27.
 */

public class ChildFoodKind {
    private int ID;

    private int PFK_ID;

    private String CHILD_TYPE;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setPFK_ID(int PFK_ID) {
        this.PFK_ID = PFK_ID;
    }

    public int getPFK_ID() {
        return this.PFK_ID;
    }

    public void setCHILD_TYPE(String CHILD_TYPE) {
        this.CHILD_TYPE = CHILD_TYPE;
    }

    public String getCHILD_TYPE() {
        return this.CHILD_TYPE;
    }
}
