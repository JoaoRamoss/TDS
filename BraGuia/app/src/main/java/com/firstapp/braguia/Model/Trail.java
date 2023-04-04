package com.firstapp.braguia.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    private final char difficulty;


    public Trail(int id, String image_src, String trail_name, String trail_description, int duration, char difficulty) {
        this.id  = id;
        this.image_src = image_src;
        this.trail_name = trail_name;
        this.trail_description = trail_description;
        this.duration = duration;
        this.difficulty = difficulty;
    }

    public int getId() {return id;}

    public String getImage_src() {return image_src;}

    public String getTrail_name() {return trail_name;}

    public String getTrail_description() {return trail_description;}

    public int getDuration() {return duration;}

    public char getDifficulty() {return difficulty;}

}
