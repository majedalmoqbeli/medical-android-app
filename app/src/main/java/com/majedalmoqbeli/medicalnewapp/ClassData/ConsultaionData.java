package com.majedalmoqbeli.medicalnewapp.ClassData;

import java.io.Serializable;

public class ConsultaionData implements Serializable {
    String cons_id,
            user_id,
            cons_title,
            cons_content,
            is_public,
            cons_date_time,
            birthday,
            gender,
            weight,
            co_a_content,
            user_name,
            u_id_doc,
            doctor_name;

    public ConsultaionData(String cons_id, String user_id, String cons_title, String cons_content,
                           String is_public, String cons_date_time,
                           String birthday,
                           String gender,
                           String weight,
                           String co_a_content, String user_name, String u_id_doc, String doctor_name) {
        this.cons_id = cons_id;
        this.user_id = user_id;
        this.cons_title = cons_title;
        this.cons_content = cons_content;
        this.is_public = is_public;
        this.cons_date_time = cons_date_time;
        this.co_a_content = co_a_content;
        this.user_name = user_name;
        this.u_id_doc = u_id_doc;
        this.doctor_name = doctor_name;
        this.birthday = birthday;
        this.gender = gender;
        this.weight = weight;
    }

    public String getCons_id() {
        return cons_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCons_title() {
        return cons_title;
    }

    public String getCons_content() {
        return cons_content;
    }

    public String getIs_public() {
        return is_public;
    }

    public String getCons_date_time() {
        return cons_date_time;
    }

    public String getCo_a_content() {
        return co_a_content;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getU_id_doc() {
        return u_id_doc;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getWeight() {
        return weight;
    }
}
