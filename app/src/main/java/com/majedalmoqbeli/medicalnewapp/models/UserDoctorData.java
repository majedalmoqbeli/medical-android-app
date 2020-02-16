package com.majedalmoqbeli.medicalnewapp.models;

import java.io.Serializable;

public class UserDoctorData implements Serializable{
String
    user_name,
    email,
    address,
    balance,
    phone,
    active,
    created_date,
    a_content,
    about,
    dep_name
    ,cons_price;

    public UserDoctorData(String user_name, String email, String address, String balance, String phone, String active, String created_date, String a_content, String about, String dep_name, String cons_price) {
        this.user_name = user_name;
        this.email = email;
        this.address = address;
        this.balance = balance;
        this.phone = phone;
        this.active = active;
        this.created_date = created_date;
        this.a_content = a_content;
        this.about = about;
        this.dep_name = dep_name;
        this.cons_price = cons_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getBalance() {
        return balance;
    }

    public String getPhone() {
        return phone;
    }

    public String getActive() {
        return active;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getA_content() {
        return a_content;
    }

    public String getAbout() {
        return about;
    }

    public String getDep_name() {
        return dep_name;
    }

    public String getCons_price() {
        return cons_price;
    }
}
