package com.example.maxgirkins.songle;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import static com.example.maxgirkins.songle.Songle.songle;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class Song {

    @Expose
    private String title;
    @Expose
    private String artist;
    @Expose
    private String youtubeLink;
    @Expose
    private Integer num;
    @Expose
    private List<Lyric> lyrics;
    @Expose
    private  Integer completed;
    @Expose
    private double distanceWalked;
    private final String TAG2 = "SongClass";

    public Song(String title, String artist, Integer num, String youtubeLink) {
        this.title = title;
        this.artist = artist;
        this.num = num;
        this.lyrics = new ArrayList<>();
        this.completed = 0;
        this.youtubeLink = youtubeLink;
        this.distanceWalked = 0.0;
//        Log.i(TAG2, "song initialised with youtube link: " + youtubeLink);
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
    public String getArtistAndTitle(){
        return title + " - " + artist;
    }

    public List<Lyric> getLyrics() {
        return lyrics;
    }
    public Integer getCompletedLyricsCount(){
        Integer count = 0;
        for (int i=0; i<lyrics.size();i++){
            if (lyrics.get(i).isCollected()){
                count +=1;
            }
        }
        return count;
    }

    public void addLyrics(List<Lyric>l){
        this.lyrics = l;
    }

    public Boolean isCompleted() {
        if (completed == 1 ){
            return true;
        } else {
            return false;
        }
    }
    public void setCompleted(){
        this.completed = 1;
    }

    public Integer getNum() {
        return num;
    }
    @Override
    public String toString(){
        String s = "";
        Integer lineNum = 1;
        for (int i=0; i<lyrics.size(); i++){
            if (lyrics.get(i).getSongPosition()[0] != lineNum){
                s = s + "\n" + lyrics.get(i).toCensoredString() +" ";
                lineNum  = lyrics.get(i).getSongPosition()[0];
            } else {
                s = s+lyrics.get(i).toCensoredString() +" ";
            }
        }
        return s;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }


    public Double getDistanceWalked() {
        if (songle.getSettings().getUnits().equals("Km")){
            return distanceWalked/1000;
        } else {
            return (0.621371*(distanceWalked/1000));
        }

    }

    public void setDistanceWalked(double distance) {
        this.distanceWalked += distance;
    }
}
