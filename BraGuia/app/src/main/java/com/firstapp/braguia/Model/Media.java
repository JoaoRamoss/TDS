package com.firstapp.braguia.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Media implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("media_file")
    private String media_file;

    @SerializedName("media_type")
    private String media_type;


    public Media () {
        id = -1;
        media_file = "";
        media_type = "";
    }

    public Media (int id, String mf, String mt) {
        this.id = id;
        this.media_type = mt;
        this.media_file = mf;
    }

    public int getId() {
        return id;
    }

    public String getMedia_file() {
        return media_file;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
}
