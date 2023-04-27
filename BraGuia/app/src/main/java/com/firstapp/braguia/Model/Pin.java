package com.firstapp.braguia.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pin implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("pin_name")
    private String pin_name;
    @SerializedName("pin_desc")
    private String pin_desc;
    @SerializedName("pin_lat")
    private double pin_lat;
    @SerializedName("pin_lng")
    private double pin_lng;
    @SerializedName("pin_alt")
    private double pin_alt;

    public Pin() {
        this.id = -1;
        this.pin_name = "";
        this.pin_desc="";
        this.pin_lat = 0.0;
        this.pin_lng = 0.0;
        this.pin_alt = 0.0;
    }

    public Pin(int id, String pin_name, String pin_desc, double pin_lat, double pin_lng, double pin_alt) {
        this.id = id;
        this.pin_name = pin_name;
        this.pin_desc = pin_desc;
        this.pin_lat = pin_lat;
        this.pin_lng = pin_lng;
        this.pin_alt = pin_alt;
    }

    public double getPin_alt() {
        return pin_alt;
    }

    public double getPin_lat() {
        return pin_lat;
    }

    public double getPin_lng() {
        return pin_lng;
    }

    public int getId() {
        return id;
    }

    public String getPin_desc() {
        return pin_desc;
    }

    public String getPin_name() {
        return pin_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPin_alt(double pin_alt) {
        this.pin_alt = pin_alt;
    }

    public void setPin_desc(String pin_desc) {
        this.pin_desc = pin_desc;
    }

    public void setPin_lat(double pin_lat) {
        this.pin_lat = pin_lat;
    }

    public void setPin_lng(double pin_lng) {
        this.pin_lng = pin_lng;
    }

    public void setPin_name(String pin_name) {
        this.pin_name = pin_name;
    }

}
