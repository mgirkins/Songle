package com.example.maxgirkins.songle;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by MaxGirkins on 27/11/2017.
 */

public class Songle extends Application implements DownloadLyricsResponse{
    private static final String TAG = "SongleSingletonClass";
    public static Songle songle;
    private SongList songs;
    private DownloadXmlTask download;
    private DownloadSongLyrics downloadsongs;
    private static  final String PREFS = "PreferencesFile";
    private SharedPreferences sharedPreferences;
    private Integer level;
    private MainActivity main;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private UserStatistics stats;
    private Settings settings;

    @Override
    public void onCreate(){
        super.onCreate();
        songle = this;
        main = new MainActivity();
        settings = new Settings();
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        songs = new SongList();
        stats = new UserStatistics();
        getData();
        downloadSongInfo();
        importSongLyrics(songs.getActiveSong().getNum(),settings.getDifficulty());

    }
    public SongList getSongs(){
        return songs;
    }
    public void resetProgress(){
        main = new MainActivity();
        settings = new Settings();
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        level = sharedPreferences.getInt("level", 0);
        settings.setDifficulty(level);
        String units = sharedPreferences.getString("units", "km");
        settings.setUnits(units);
        songs = new SongList();
        stats = new UserStatistics();
        downloadSongInfo();
        importSongLyrics(songs.getActiveSong().getNum(),level);
    }
    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    public void getData(){

        String jsonSongs = sharedPreferences.getString("Songs", "");
        String jsonSettings = sharedPreferences.getString("Settings", "");
        String jsonStats = sharedPreferences.getString("Stats", "");
        this.stats = gson.fromJson(jsonStats, UserStatistics.class);
        Log.i(TAG,jsonStats);
        this.songs = gson.fromJson(jsonSongs, SongList.class);
        Log.i(TAG,jsonSongs);
        this.settings = gson.fromJson(jsonSettings,Settings.class);
        Log.i(TAG,jsonSettings);
        Log.i(TAG,"Data's back");
        //
        //String jsonStats = sharedPreferences.getString("Stats", "");
        //
        //String jsonSettings = sharedPreferences.getString("Settings", "");
        //
    }
    public void saveData() throws IOException {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonSongs = gson.toJson(songs);
        Log.i(TAG,jsonSongs);
        editor.putString("Songs", jsonSongs);
        String jsonStats = gson.toJson(stats);
        Log.i(TAG,jsonStats);
        editor.putString("Stats", jsonStats);
        String jsonSettings = gson.toJson(settings);
        Log.i(TAG,jsonSettings);
        editor.putString("Settings", jsonSettings);
        editor.apply();
    }
    public void downloadSongInfo(){
        download = new DownloadXmlTask();
        try {
            Log.i(TAG, "songs downloading");
            SongList temp = download.execute("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml").get();
            if (temp.getNumSongs() != songs.getNumSongs()){
                for (int i=songs.getNumSongs(); i<temp.getNumSongs(); i++){
                    songs.addSong(temp.getSong(i));
                }
            }
        } catch (InterruptedException | ExecutionException i){
            i.printStackTrace();
        }

    }
    public void importSongLyrics(Integer num, Integer level){
        String numForm = Integer.toString(num+1);
        if (numForm.length() == 1){
            numForm = "0" + numForm;
        }
        String[] strings = {"http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + numForm + "/lyrics.txt","http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+numForm+"/map"+(level+1)+".kml"};

        try {
            //slightly hacked way of making sure new lyrics download doesn't wipe progress
            //make list of all completed lyrics then download new lyrics and set all the same lyrics
            //completed.
            if (songs.getActiveSong().getLyrics().size() != 0){
                ArrayList<Integer> completed_lyrics = new ArrayList<>();
                Integer len = songs.getActiveSong().getLyrics().size();
                for (int i=0; i<len; i++){
                    if (songs.getActiveSong().getLyrics().get(i).isCollected()){
                        completed_lyrics.add(i);
                    }
                }
                downloadsongs = new DownloadSongLyrics();
                this.songs.getSong(num).addLyrics(downloadsongs.execute(strings).get());
                for (int i=0; i<len; i++){
                    if (completed_lyrics.contains(i)){
                        songs.getActiveSong().getLyrics().get(i).setCollected();
                    }
                }
            } else {
                downloadsongs = new DownloadSongLyrics();
                this.songs.getSong(num).addLyrics(downloadsongs.execute(strings).get());
            }

        } catch (InterruptedException | ExecutionException i){
            i.printStackTrace();
        }

    }
    @Override
    public void onLyricsDownloaded(List<Lyric> list) {
        Log.i(TAG, "lyrics Downloaded");
        try {
            main.onLyricsDownloaded();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public UserStatistics getStats() {
        return stats;
    }
    public Settings getSettings(){
        return settings;
    }
}
