package com.example.disneytripplanner;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Park {
    String id;
    String queryId;
    String parkName;
    GeoPoint geoLocation;

    public Park() {

    }

    public Park(String id, String queryId, String parkName, GeoPoint geoLocation) {
        this.id = id;
        this.queryId = queryId;
        this.parkName = parkName;
        this.geoLocation = geoLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Override
    public String toString() {
        return "Park{" +
                "id='" + id + '\'' +
                ", queryId='" + queryId + '\'' +
                ", parkName='" + parkName + '\'' +
                ", geoLocation=" + geoLocation +
                '}';
    }
}
