package com.firstapp.braguia.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "trail_table")
public class Trail {
    @PrimaryKey
    @ColumnInfo(name = "trail_id")
    private final int id;

    @ColumnInfo(name = "image_source")
    private final String image_src;

    @ColumnInfo(name = "trail_name")
    private final String trail_name;

    @ColumnInfo(name="trail_description")
    private final String trail_description;

    @ColumnInfo(name = "duration")
    private final int duration;

    @ColumnInfo(name = "difficulty")
    private final String difficulty;


    public Trail(int id, String image_src, String trail_name, String trail_description, int duration, String difficulty) {
        this.id  = id;
        this.image_src = image_src;
        this.trail_name = trail_name;
        this.trail_description = trail_description;
        this.duration = duration;
        this.difficulty = difficulty;
    }

    public Trail(JSONObject jo) throws JSONException, NoSuchFieldException, IllegalAccessException {
        this.id = jo.getInt("id");
        this.image_src = jo.getString("trail_img");
        this.trail_name = jo.getString("trail_name");
        this.trail_description = jo.getString("trail_desc");
        this.duration = jo.getInt("trail_duration");
        this.difficulty = jo.getString("trail_difficulty");
    }

    public int getId() {return id;}

    public String getImage_src() {return image_src;}

    public String getTrail_name() {return trail_name;}

    public String getTrail_description() {return trail_description;}

    public int getDuration() {return duration;}

    public String getDifficulty() {return difficulty;}

    public boolean equals(Trail t){
        return (this.id == t.getId()) && (this.getImage_src().equals(t.getImage_src())) &&
                (this.trail_name.equals(t.getTrail_name())) && (this.trail_description.equals(t.getTrail_description()))
                && (this.duration == t.getDuration()) && (this.difficulty.equals(t.getDifficulty()));
    }
}
