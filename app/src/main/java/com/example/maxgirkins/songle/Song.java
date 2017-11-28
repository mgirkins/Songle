package com.example.maxgirkins.songle;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class Song {

    private String title;
    private String artist;
    private Integer num;
    private List<Lyric> lyrics;
    private  Integer completed;
    private Long completedAt;
    private final String TAG2 = "SongClass";

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
    public Integer getCompletedLyricsCount(){
        Integer count = 0;
        for (int i=0; i<lyrics.size();i++){
            if (lyrics.get(i).isCollected()){
                count +=1;
            }
        }
        return count;
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

    public Long getCompletedAt() {
        return completedAt;
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
        Log.i(TAG2,s);
        return s;
    }
}
