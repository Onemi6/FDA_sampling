package com.fda_sampling.model;

public class ImageInfoAdd {
    private String IMG_TYPE;

    private String PATH;

    public ImageInfoAdd(String IMG_TYPE, String PATH) {
        this.IMG_TYPE = IMG_TYPE;
        this.PATH = PATH;
    }

    public void setIMG_TYPE(String IMG_TYPE) {
        this.IMG_TYPE = IMG_TYPE;
    }

    public String getIMG_TYPE() {
        return this.IMG_TYPE;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getPATH() {
        return this.PATH;
    }
}
