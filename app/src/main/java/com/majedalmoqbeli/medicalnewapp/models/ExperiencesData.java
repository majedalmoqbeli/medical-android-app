package com.majedalmoqbeli.medicalnewapp.models;

public class ExperiencesData {
    String expID , expName;

    public ExperiencesData(String expID, String expName) {
        this.expID = expID;
        this.expName = expName;
    }

    public String getExpID() {
        return expID;
    }

    public String getExpName() {
        return expName;
    }
}
