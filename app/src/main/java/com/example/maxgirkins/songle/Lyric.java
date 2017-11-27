package com.example.maxgirkins.songle;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Array;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class Lyric implements java.io.Serializable {
    private String word;
    private Boolean collected;
    private Date collectedAt;
    private Integer[] songPosition;
    private List<LatLng> coords;
    private Marker mapMarker;
    private List<String> classification;
    private final String TAG = "LyricClass";

    public Lyric(String word, Integer[] songPosition){
        this.word = word;
        this.songPosition = songPosition;
        this.collected = false;
        this.collectedAt = null;
        this.coords = new ArrayList<>(Arrays.asList(new LatLng(0,0),new LatLng(0,0),new LatLng(0,0),new LatLng(0,0),new LatLng(0,0)));
        this.classification = new ArrayList<>(Arrays.asList("unclassified", "unclassified", "unclassified","unclassified","unclassified"));
    }

    public String getLyric(){
        return word;
    }
    public void setCollectedAt(Date now){
        collected = true;
        collectedAt = now;
    }

    public Marker getMapMarker() {
        return mapMarker;
    }

    public void setMapMarker(Marker marker){
        mapMarker = marker;
    }
    public void setCoords(LatLng l, Integer level){
        coords.set(level, l);
    }
    public LatLng getCoords(Integer level) {
        return coords.get(level);
    }
    public void setClassification(String classification, Integer level){
        this.classification.set(level,classification);
    }
    public String getClassification(Integer level){
        return classification.get(level);
    }
    public Boolean isCollected(){
        return collected;
    }
    public Date getCollectedAt() {
        return collectedAt;
    }
    public Integer[] getSongPosition() {
        return songPosition;
    }
    public String toString(){
        return word + ": " + songPosition[0].toString() +","  +songPosition[1].toString()+"  " + coords.toString() + "..." + classification.toString();
    }

}
