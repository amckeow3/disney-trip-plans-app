package com.example.disneytripplanner;

public class Trip {
    String tripName;
    String resort;
    String startDate;
    String endDate;
    String description;

    public Trip(String tripName, String resort, String startDate, String endDate, String description) {
        this.tripName = tripName;
        this.resort = resort;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public Trip(String tripName, String resort, String startDate, String endDate) {
        this.tripName = tripName;
        this.resort = resort;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Trip() {

    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getResort() {
        return resort;
    }

    public void setResort(String resort) {
        this.resort = resort;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripName='" + tripName + '\'' +
                ", resort='" + resort + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
