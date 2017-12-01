package com.example.maxgirkins.songle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.maxgirkins.songle.Songle.songle;

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
        return songs.get(num);
    }
    public List<Song> getAllSongs(){
        return songs;
    }
    public Integer getCompletedSongsCount(){
        Integer count = 0;
        for (int i=0; i<songs.size(); i++){
            if (songs.get(i).isCompleted()){
                count += 1;
            }
        }
        return count;
    }
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

        if (activeSong == -1){
            newActiveSong();
            return getActiveSong();
        } else {
            return songs.get(activeSong);
        }


    }
    public void newActiveSong(){
        activeSong = (int) (Math.random() * uncompletedSongs().size());
        Log.i(TAG1, songs.get(uncompletedSongs().get(activeSong)).getTitle());
        songle.importSongLyrics(activeSong,songle.getSettings().getDifficulty());
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
