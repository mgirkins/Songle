package com.example.maxgirkins.songle;

import android.icu.text.DateFormat;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.sql.Date;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class Lyric implements java.io.Serializable {

    private String word;
    private Boolean collected;
    private Date collectedAt;
    private Array songPosition;
    private LatLng coords;
    private List<String> classification;

    public Lyric(String word, Array songPosition, LatLng coords, List<String> classification){
        this.word = word;
        this.songPosition = songPosition;
        this.coords = coords;
        this.collected = false;
        this.collectedAt = null;
        this.classification = classification;
    }

    public String getLyric(){
        return word;
    }

    public void setCollectedAt(Date now){
        collected = true;
        collectedAt = now;
    }

    public Boolean isCollected(){
        return collected;
    }


    public Date getCollectedAt() {
        return collectedAt;
    }

    public Array getSongPosition() {
        return songPosition;
    }

    public LatLng getCoords() {
        return coords;
    }

    public String getClassification(int level) {
        return classification.get(level);
    }
    public void setClassification(String classification, int level){
        this.classification.set(level, classification);
    }
}
