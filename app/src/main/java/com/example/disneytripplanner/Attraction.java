package com.example.disneytripplanner;

public class Attraction {
    String name;
    Boolean isOpen;
    String waitTime;
    String land;

    public Attraction() {

    }

    public Attraction(String name, Boolean isOpen, String waitTime, String land) {
        this.name = name;
        this.isOpen = isOpen;
        this.waitTime = waitTime;
        this.land = land;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "name='" + name + '\'' +
                ", isOpen=" + isOpen +
                ", waitTime='" + waitTime + '\'' +
                ", land='" + land + '\'' +
                '}';
    }
}
