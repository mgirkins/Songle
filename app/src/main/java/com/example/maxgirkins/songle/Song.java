package com.example.maxgirkins.songle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class Song implements java.io.Serializable {

    private String title;
    private String artist;
    private Integer num;
    private List<Lyric> lyrics;
    private  Integer completed;
    private Date completedAt;

    public Song(String title, String artist, Integer num) {
        this.title = title;
        this.artist = artist;
        this.num = num;
        this.lyrics = new ArrayList<>();
        this.completed = 0;
        this.completedAt = null;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public List<Lyric> getLyrics() {
        return lyrics;
    }
    public void setLyrics(List<Lyric> l){
        this.lyrics = l;
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

    public Date getCompletedAt() {
        return completedAt;
    }

    public Integer getNum() {
        return num;
    }
    @Override
    public String toString(){
        String s = "";
        for (int i=0; i<lyrics.size(); i++){
            s = s+lyrics.get(i).toString();
        }
        return s;
    }
}
