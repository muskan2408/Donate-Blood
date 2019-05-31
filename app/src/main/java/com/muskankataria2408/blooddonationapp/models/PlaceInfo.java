package com.muskankataria2408.blooddonationapp.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String name;
    private String address;
    private String id;
    private LatLng latLng;
    private float rating;
    private String phoneNumber;
    private String attributions;
    private Uri websiteUri;

    public PlaceInfo(String name, String address, String id, LatLng latLng, float rating, String phoneNumber, String attributions, Uri websiteUri) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latLng = latLng;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.attributions = attributions;
        this.websiteUri = websiteUri;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", attributions='" + attributions + '\'' +
                ", websiteUri=" + websiteUri +
                '}';
    }
}
