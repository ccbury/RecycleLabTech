//Add to package
package com.ccbury.recyclelabtech;
// This class simply creates an object that will obtain data from the database and store it for use by any of 3 methods.
public class products {
    //Declare variables
    public String name, description, manufactor, barcodefmt, image, barcodecnt;

    //Getters and Setters
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

    public String getBarcodefmt() { return barcodefmt; }
    public void setBarcodefmt(String barcodefmt) { this.barcodefmt = barcodefmt; }

    public String getBarcodecnt() { return barcodecnt; }
    public void setBarcodecnt(String barcodecnt) { this.barcodecnt = barcodecnt; }

    public String getImage() {
        return image;
    }
    public void setImage(String image) { this.image = image; }

    //Constructor
    public products(String name, String description, String manufactor, String barcodefmt, String image, String barcodecnt) {
        this.name = name;
        this.description = description;
        this.manufactor = manufactor;
        this.barcodefmt = barcodefmt;
        this.image = image;
    }
    //Empty constructor
    public products(){}

}//End class
