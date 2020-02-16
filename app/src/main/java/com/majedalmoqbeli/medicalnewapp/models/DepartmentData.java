package com.majedalmoqbeli.medicalnewapp.models;

public class DepartmentData {
    String depID , DepNameAr , DepNameEn ,is_active,img_url ;

    public DepartmentData(String depID, String depNameAr, String depNameEn, String is_active, String img_url) {
        this.depID = depID;
        this.DepNameAr = depNameAr;
        this.DepNameEn = depNameEn;
        this.is_active=is_active;
        this.img_url = img_url;
    }

    public DepartmentData(String depID, String depNameAr) {
        this.depID = depID;
        this.DepNameAr = depNameAr;
    }


    public String getDepID() {
        return depID;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getDepNameAr() {
        return DepNameAr;
    }

    public String getDepNameEn() {
        return DepNameEn;
    }

    public String getIs_active() {
        return is_active;
    }
}
