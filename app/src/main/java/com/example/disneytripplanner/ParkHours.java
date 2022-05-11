package com.example.disneytripplanner;

public class ParkHours {
    String date;
    String openingTime;
    String closingTime;

    public ParkHours() {

    }

    public ParkHours(String date, String openingTime, String closingTime) {
        this.date = date;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
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

    @Override
    public String toString() {
        return "ParkHours{" +
                "date='" + date + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                '}';
    }
}
