package com.example.disneytripplanner.models;

public class ParkHours {
    String date;
    String openingTime;
    String closingTime;
    String park;

    public ParkHours() {

    }

    public ParkHours(String date, String openingTime, String closingTime, String park) {
        this.date = date;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.park = park;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    @Override
    public String toString() {
        return "ParkHours{" +
                "date='" + date + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", park='" + park + '\'' +
                '}';
    }
}
