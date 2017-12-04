package com.example.maxgirkins.songle;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.maxgirkins.songle.Songle.songle;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class SongList {
    //expose data to Gson
    @Expose
    private List<Song> songs;
    @Expose
    private Integer activeSongNum;
    public SongList(){
        //activesong starts as -1 so that a new active song is called when data exists.
        activeSongNum = -1;
        songs = new ArrayList<>();
    }

    public Song getSong(Integer num){
        return songs.get(num);
    }
    //count songs that have been completed, return count.
    public Integer getCompletedSongsCount(){
        Integer count = 0;
        for (int i=0; i<songs.size(); i++){
            if (songs.get(i).isCompleted()){
                count += 1;
            }
        }
        return count;
    }
    //prettified list of titles and artists of all songs.
    public List<String> getTitlesAndArtist(){
        List<String> titles = new ArrayList<>();
        for (int i = 0; i< songs.size(); i++){
            titles.add(songs.get(i).getTitle() + " - " + songs.get(i).getArtist());
        }
        return titles;
    }
    public List<String> getCompletedTitles(){
        List<String> titles = new ArrayList<>();
        for (int i = 0; i< songs.size(); i++){
            if (songs.get(i).isCompleted()){
            titles.add(songs.get(i).getTitle());
            }
        }
        return titles;
    }
    public List<String> getCompletedArtists(){
        List<String> artists = new ArrayList<>();
        for (int i = 0; i< songs.size(); i++){
            if (songs.get(i).isCompleted()){
                artists.add(songs.get(i).getArtist());
            }
        }
        return artists;
    }
    public List<String> getCompletedYoutubeLinks(){
        List<String> links = new ArrayList<>();
        for (int i = 0; i< songs.size(); i++){
            if (songs.get(i).isCompleted()){
                links.add(songs.get(i).getYoutubeLink());
            }
        }
        return links;
    }

    public Integer getNumSongs(){
        return songs.size();
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public Song getActiveSong(){

        if (activeSongNum == -1){
            newActiveSong();
            return getActiveSong();
        } else {
            return songs.get(activeSongNum);
        }


    }
    //pick random new song that hasn't been completed yet and isn't the current song
    //then download the lyrics for that song.
    public void newActiveSong(){
        Random rand = new Random();
        Integer num = rand.nextInt((uncompletedSongs().size()) + 1);
        if (num == activeSongNum){
            newActiveSong();
        } else {
            activeSongNum = uncompletedSongs().get(num);
        }
        songle.importSongLyrics(activeSongNum,songle.getSettings().getDifficulty());
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
