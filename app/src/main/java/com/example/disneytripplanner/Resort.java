package com.example.disneytripplanner;

import java.util.ArrayList;

public class Resort {
    String id;
    String resortName;
    ArrayList<Park> parks = new ArrayList<>();

    public Resort() {

    }

    public Resort(String id, String resortName, ArrayList<Park> parks) {
        this.id = id;
        this.resortName = resortName;
        this.parks = parks;
    }

    public Resort(String id, String resortName) {
        this.id = id;
        this.resortName = resortName;
    }

    public String getResortName() {
        return resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public ArrayList<Park> getParks() {
        return parks;
    }

    public void setParks(ArrayList<Park> parks) {
        this.parks = parks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Resort{" +
                "id='" + id + '\'' +
                ", resortName='" + resortName + '\'' +
                ", parks=" + parks +
                '}';
    }
}
