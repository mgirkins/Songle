package com.example.maxgirkins.songle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class SongList implements java.io.Serializable {
    private List<Song> songs = new ArrayList<>();

    public SongList(){
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<String> getTitles(){
        List<String> titles = new ArrayList<>();
        for (int i = 0; i< songs.size(); i++){
            titles.add(songs.get(i).getTitle());
        }
        return titles;
    }

    public Integer getNumSongs(){
        return songs.size();
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }
}
