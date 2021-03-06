package com.fda_sampling.model;

public class ImageInfo {
    private int ID;

    private String APPLY_NO;

    private String IMG_TYPE;

    private int IMG_CONTENT_LEN;

    private String PATH;

    public ImageInfo(String IMG_TYPE, String PATH) {
        this.IMG_TYPE = IMG_TYPE;
        this.PATH = PATH;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setAPPLY_NO(String APPLY_NO) {
        this.APPLY_NO = APPLY_NO;
    }

    public String getAPPLY_NO() {
        return this.APPLY_NO;
    }

    public void setIMG_TYPE(String IMG_TYPE) {
        this.IMG_TYPE = IMG_TYPE;
    }

    public String getIMG_TYPE() {
        return this.IMG_TYPE;
    }

    public void setIMG_CONTENT_LEN(int IMG_CONTENT_LEN) {
        this.IMG_CONTENT_LEN = IMG_CONTENT_LEN;
    }

    public int getIMG_CONTENT_LEN() {
        return this.IMG_CONTENT_LEN;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getPATH() {
        return this.PATH;
    }
}
