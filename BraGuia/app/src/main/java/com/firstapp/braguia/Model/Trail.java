package com.firstapp.braguia.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trail_table")
public class Trail implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @Ignore
    @SerializedName("edges")
    private List<Edge> edges;
    @SerializedName("trail_img")
    @ColumnInfo(name = "image_source")
    private  String trail_img;

    @ColumnInfo(name = "trail_name")
    @SerializedName("trail_name")
    private  String trail_name;

    @ColumnInfo(name="trail_description")
    @SerializedName("trail_desc")
    private  String trail_desc;

    @ColumnInfo(name = "trail_duration")
    @SerializedName("trail_duration")
    private  int trail_duration;

    @ColumnInfo(name = "trail_difficulty")
    @SerializedName("trail_difficulty")
    private  String trail_difficulty;

    public Trail(){
        this.id = -1;
        this.edges = new ArrayList<>();
        this.trail_img = "";
        this.trail_name = "";
        this.trail_desc = "";
        this.trail_duration = 0;
        this.trail_difficulty = "";
    }
    public Trail(int id, List<Edge> edges, String image_src, String trail_name, String trail_description, int duration, String difficulty) {
        this.id  = id;
        this.edges = new ArrayList<>(edges);
        this.trail_img = image_src;
        this.trail_name = trail_name;
        this.trail_desc = trail_description;
        this.trail_duration = duration;
        this.trail_difficulty = difficulty;
    }

    public Trail(JSONObject jo) throws JSONException, NoSuchFieldException, IllegalAccessException {
        this.id = jo.getInt("id");
        this.edges = new ArrayList<>();
        this.trail_img = jo.getString("trail_img");
        this.trail_name = jo.getString("trail_name");
        this.trail_desc = jo.getString("trail_desc");
        this.trail_duration = jo.getInt("trail_duration");
        this.trail_difficulty = jo.getString("trail_difficulty");
    }

    //Getters
    public int getId() {return id;}

    public String getTrail_img() {return trail_img;}

    public String getTrail_name() {return trail_name;}

    public String getTrail_desc() {return trail_desc;}

    public int getTrail_duration() {return trail_duration;}

    public String getTrail_difficulty() {return trail_difficulty;}

    public List<Edge> getEdges() {
        return edges;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }

    public void setTrail_desc(String trail_description) {
        this.trail_desc = trail_description;
    }

    public void setTrail_difficulty(String trail_difficulty) {
        this.trail_difficulty = trail_difficulty;
    }

    public void setTrail_duration(int trail_duration) {
        this.trail_duration = trail_duration;
    }

    public void setTrail_name(String trail_name) {
        this.trail_name = trail_name;
    }

    public void setTrail_img (String src){
        this.trail_img = src;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    //TODO
    //Update nesta função para comparar as vars de instancia que foram adicionadas
    public boolean equals(Trail t){
        return (this.id == t.getId()) && (this.getTrail_img().equals(t.getTrail_img())) &&
                (this.trail_name.equals(t.getTrail_name())) && (this.trail_desc.equals(t.getTrail_desc()))
                && (this.trail_duration == t.getTrail_duration()) && (this.trail_difficulty.equals(t.getTrail_difficulty()));
    }
}
