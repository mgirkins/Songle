package com.example.maxgirkins.songle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class SongList {
    private List<Song> songs;
    private Integer activeSong;
    protected final String TAG1 = "SonglistClass";

    public SongList(){
        activeSong = -1;
        songs = new ArrayList<>();
    }

    public Song getSong(Integer num){
        return songs.get(num-1);
    }
    public List<String> getTitles(){
        List<String> titles = new ArrayList<>();
        for (int i = 0; i< songs.size(); i++){
            titles.add(songs.get(i).getTitle() + " - " + songs.get(i).getArtist());
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

        if (activeSong == -1){
            return newActiveSong();
        } else {
            return songs.get(activeSong);
        }


    }
    public Song newActiveSong(){
        activeSong = (int) (Math.random() * uncompletedSongs().size());
        Log.i(TAG1, songs.get(uncompletedSongs().get(activeSong)).getTitle());
        return songs.get(uncompletedSongs().get(activeSong));
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
