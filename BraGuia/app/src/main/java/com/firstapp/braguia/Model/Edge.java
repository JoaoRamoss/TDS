package com.firstapp.braguia.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kotlin.jvm.internal.SerializedIr;

public class Edge implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("edge_start")
    private Pin edge_start;
    @SerializedName("edge_end")
    private Pin edge_end;
    @SerializedName("edge_transport")
    private String edge_transport;
    @SerializedName("edge_duration")
    private int edge_duration;
    @SerializedName("edge_desc")
    private String edge_desc;
    @SerializedName("edge_trail")
    private int edge_trail;

    public Edge(){
        this.id = -1;
        this.edge_start = new Pin();
        this.edge_end = new Pin();
        this.edge_transport = "";
        this.edge_duration = 0;
        this.edge_desc = "";
        this.edge_trail = 0;
    }

    public Edge (int id, Pin edge_start, Pin edge_end, String edge_transport, int edge_duration, String edge_desc, int edge_trail) {
        this.id = id;
        this.edge_trail = edge_trail;
        this.edge_start = edge_start;
        this.edge_end = edge_end;
        this.edge_transport = edge_transport;
        this.edge_duration = edge_duration;
        this.edge_desc = edge_desc;
    }

    public int getId() {
        return id;
    }

    // Getters
    public int getEdge_duration() {
        return edge_duration;
    }

    public int getEdge_trail() {
        return edge_trail;
    }

    public Pin getEdge_end() {
        return edge_end;
    }

    public Pin getEdge_start() {
        return edge_start;
    }

    public String getEdge_desc() {
        return edge_desc;
    }

    public String getEdge_transport() {
        return edge_transport;
    }

    //Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setEdge_desc(String edge_desc) {
        this.edge_desc = edge_desc;
    }

    public void setEdge_duration(int edge_duration) {
        this.edge_duration = edge_duration;
    }

    public void setEdge_end(Pin edge_end) {
        this.edge_end = edge_end;
    }

    public void setEdge_start(Pin edge_start) {
        this.edge_start = edge_start;
    }

    public void setEdge_trail(int edge_trail) {
        this.edge_trail = edge_trail;
    }

    public void setEdge_transport(String edge_transport) {
        this.edge_transport = edge_transport;
    }
}
