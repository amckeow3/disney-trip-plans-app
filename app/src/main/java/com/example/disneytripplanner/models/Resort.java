package com.example.disneytripplanner.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Resort implements Serializable {
    String id;
    String queryId;
    String slug;
    String resortName;
    ArrayList<Park> parks = new ArrayList<>();

    public Resort() {

    }

    public Resort(String id, String resortName) {
        this.id = id;
        this.resortName = resortName;
    }

    public Resort(String id, String queryId, String slug, String resortName, ArrayList<Park> parks) {
        this.id = id;
        this.queryId = queryId;
        this.slug = slug;
        this.resortName = resortName;
        this.parks = parks;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
                ", queryId='" + queryId + '\'' +
                ", slug='" + slug + '\'' +
                ", resortName='" + resortName + '\'' +
                ", parks=" + parks +
                '}';
    }

}
