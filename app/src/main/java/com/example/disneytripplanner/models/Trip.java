package com.example.disneytripplanner.models;

public class Trip {
    String tripName;
    String startDate;
    String endDate;
    String description;

    public Trip(String tripName, String startDate, String endDate, String description) {
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public Trip() {

    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
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
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
