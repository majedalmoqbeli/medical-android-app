package com.majedalmoqbeli.medicalnewapp.ClassData;

public class DoctorData {
  String u_id_doc,about,depart_id,address,cons_price,user_name,email,
          place,period,qualification,img_url,depart_name,depart_en_name ,is_active;

    public DoctorData(String u_id_doc, String about, String depart_id, String address, String cons_price, String user_name, String email, String place, String period, String qualification, String img_url, String depart_name, String depart_en_name, String is_active) {
        this.u_id_doc = u_id_doc;
        this.about = about;
        this.depart_id = depart_id;
        this.address = address;
        this.cons_price = cons_price;
        this.user_name = user_name;
        this.email = email;
        this.place = place;
        this.period = period;
        this.qualification = qualification;
        this.img_url = img_url;
        this.depart_name = depart_name;
        this.depart_en_name = depart_en_name;
        this.is_active = is_active;
    }

    public String getU_id_doc() {
        return u_id_doc;
    }

    public String getAbout() {
        return about;
    }

    public String getDepart_id() {
        return depart_id;
    }

    public String getAddress() {
        return address;
    }

    public String getCons_price() {
        return cons_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPlace() {
        return place;
    }

    public String getPeriod() {
        return period;
    }

    public String getQualification() {
        return qualification;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getDepart_name() {
        return depart_name;
    }

    public String getDepart_en_name() {
        return depart_en_name;
    }

    public String getIs_active() {
        return is_active;
    }
}
