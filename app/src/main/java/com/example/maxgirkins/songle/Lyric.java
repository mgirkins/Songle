package com.example.maxgirkins.songle;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.sql.Date;
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
    private List<String> classification;

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
    public void setCoords(LatLng l, Integer level){
        coords.set(level-1, l);
        Log.i("NewCoords", coords.toString());
    }
    public LatLng getCoords(Integer level) {
        return coords.get(level);
    }
    public void setClassification(String classification, Integer level){
        Log.i("Lyricset", classification + " " + level.toString());
        Log.i("Lyricset", this.classification.toString());
        this.classification.set(level-1,classification);
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
        Log.i("LricClass", "getsongPos called");
        return songPosition;
    }

}
