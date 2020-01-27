package com.majedalmoqbeli.medicalnewapp.ClassData;

public class CommentData {
    String comment_id,
            cons_id,
            user_id,
            comment,
            comment_data,
            has_img,
            user_name,
            a_content;

    public CommentData(String comment_id, String cons_id, String user_id, String comment, String comment_data ,String has_img, String user_name, String a_content) {
        this.comment_id = comment_id;
        this.cons_id = cons_id;
        this.user_id = user_id;
        this.comment = comment;
        this.comment_data = comment_data;
        this.user_name = user_name;
        this.a_content = a_content;
        this.has_img=has_img;
    }

    public String getHas_img() {
        return has_img;
    }

    public String getComment_id() {
        return comment_id;
    }

    public String getCons_id() {
        return cons_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getComment() {
        return comment;
    }

    public String getComment_data() {
        return comment_data;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getA_content() {
        return a_content;
    }
}
