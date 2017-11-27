package com.example.maxgirkins.songle;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

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
    private SharedPreferences settings;
    private Integer level;
    private MainActivity main;


    @Override
    public void onCreate(){
        songle = this;
        main = new MainActivity();
        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        level = settings.getInt("level", 4);
        songs = new SongList();
        super.onCreate();
        getData();
        downloadSongInfo();
        importSongLyrics(songs.getActiveSong().getNum(),songs,level);


    }
    public Integer getLevel(){
        return level;
    }

    public SongList getSongs() {
        getData();
        return songs;
    }

    public void setSongs(SongList songs) {
        this.songs = songs;
        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SharedPreferences getSettings(){
        return settings;
    }

    public void getData(){
        Gson gson = new Gson();
        String json = settings.getString("Data", "");
        this.songs = gson.fromJson(json, SongList.class);
        Log.i(TAG, "i'm back");
    }
    public void saveData() throws IOException {
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(songs);
        editor.putString("Data", json);
        editor.apply();
        Log.i(TAG, "Data Saved");
    }
    public void downloadSongInfo(){
        download = new DownloadXmlTask();
        try {

            this.songs = download.execute("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml").get();
        } catch (InterruptedException | ExecutionException i){
            i.printStackTrace();
        }

    }
    public void importSongLyrics(Integer num, SongList songList, Integer level){
        String numForm = num.toString();
        if (numForm.length() == 1){
            numForm = "0" + numForm;
        }
        String[] strings = {"http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + numForm + "/lyrics.txt","http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+numForm+"/map"+(level+1)+".kml"};

        try {
            downloadsongs = new DownloadSongLyrics(level, songList);
            this.songs.getSong(num).addLyrics(downloadsongs.execute(strings).get());
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
}
