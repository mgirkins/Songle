package com.example.maxgirkins.songle;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.sql.Array;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */
//Lyric container class
public class Lyric {
    //expose to Gson
    @Expose
    //lyric
    private String word;
    @Expose
    private Boolean collected;
    @Expose
    //position in the song eg line 4 word 3 is [4,3]
    private Integer[] songPosition;
    @Expose
    //coordinates of point at each level
    private List<LatLng> coords;
    //Marker can't be exposed to Gson as its superclass is not compatible
    //it doesn't matter though as the markers don't need to be saved
    private Marker mapMarker;
    @Expose
    //string classification of lyric for each level ie. notInteresting
    private List<String> classification;

    public Lyric(String word, Integer[] songPosition){
        this.word = word;
        this.songPosition = songPosition;
        this.coords = new ArrayList<>(Arrays.asList(new LatLng(0,0),new LatLng(0,0),new LatLng(0,0),new LatLng(0,0),new LatLng(0,0)));
        this.classification = new ArrayList<>(Arrays.asList("unclassified", "unclassified", "unclassified","unclassified","unclassified"));
        this.collected = false;
    }

    public String getLyric(){
        return word;
    }

    public void setCollected(){
        this.collected = true;
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

    public Integer[] getSongPosition() {
        return songPosition;
    }
    //used during testing early on
    public String toString(){
        return word + ": " + songPosition[0].toString() +","  +songPosition[1].toString()+"  " + coords.toString() + "..." + classification.toString();
    }
    //used for the collected words activity to show only completed lyrics. if lyric is not completed
    //it shows as a black box for each character so user can visualize the song word by word.
    public String toCensoredString(){
        if (collected){
            return word;
        } else {
            String str = "";
            for (int i=0;i<word.length();i++){
                str = str + '\u25A0';
            }
            return str;
        }
    }

}
