package com.example.maxgirkins.songle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class SongList implements java.io.Serializable {
    private List<Song> songs;
    private Integer activeSong;

    public SongList(){
        activeSong = 0;
        songs = new ArrayList<>();
    }

    public List<Song> getSongs() {
        return songs;
    }
    public Song getSong(Integer num){
        return songs.get(num);
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

    public Song getActiveSong(){
        return songs.get(activeSong);
    }
    public Song newActiveSong(){
        Random random = new Random();
        if (activeSong == 0){
            activeSong = random.nextInt(uncompletedSongs().size());
            return songs.get(activeSong);
        } else {
            return songs.get(activeSong);
        }
    }

    private List<Integer> uncompletedSongs(){
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i<songs.size(); i++){
            if (!songs.get(i).isCompleted()){
                indices.add(i);
            }
        }
        return  indices;
    }
}
