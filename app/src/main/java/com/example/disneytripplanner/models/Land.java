package com.example.disneytripplanner.models;

import java.util.List;

public class Land {
    String name;
    List<Attraction> attractions;

    public Land() {

    }

    public Land(String name, List<Attraction> attractions) {
        this.name = name;
        this.attractions = attractions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    @Override
    public String toString() {
        return "Land{" +
                "name='" + name + '\'' +
                ", attractions=" + attractions +
                '}';
    }
}
