package com.example.recyclelabtech;

public class products {
    public String name, description, manufactor, barcodefmt, image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufactor() {
        return manufactor;
    }

    public void setManufactor(String manufactor) {
        this.manufactor = manufactor;
    }

    public String getBarcodefmt() {
        return barcodefmt;
    }

    public void setBarcodefmt(String barcodefmt) {
        this.barcodefmt = barcodefmt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public products(String name, String description, String manufactor, String barcodefmt, String image) {
        this.name = name;
        this.description = description;
        this.manufactor = manufactor;
        this.barcodefmt = barcodefmt;
        this.image = image;
    }
    public products(){}

}
