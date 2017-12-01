package com.example.maxgirkins.songle;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
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
    private Gson gson = new Gson();
    private UserStatistics stats;
    private Settings settings;


    @Override
    public void onCreate(){
        super.onCreate();
        songle = this;
        main = new MainActivity();
        settings = new Settings();
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        level = sharedPreferences.getInt("level", 0);
        settings.setDifficulty(level);
        String units = sharedPreferences.getString("units", "km");
        settings.setUnits(units);
        songs = new SongList();
        stats = new UserStatistics();
        getData();
        downloadSongInfo();
        importSongLyrics(songs.getActiveSong().getNum(),level);

    }
    public SongList getSongsFirstRun() {
        getData();
        return songs;
    }
    public SongList getSongs(){
        return songs;
    }
    public void resetProgress(){
        main = new MainActivity();
        settings = new Settings();
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        level = sharedPreferences.getInt("level", 4);
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
        String jsonSongs = sharedPreferences.getString("Data", "");
        this.songs = gson.fromJson(jsonSongs, SongList.class);
        //String jsonStats = sharedPreferences.getString("Stats", "");
        //this.stats = gson.fromJson(jsonStats, UserStatistics.class);
        //String jsonSettings = sharedPreferences.getString("Settings", "");
        //this.settings = gson.fromJson(jsonSettings,Settings.class);
    }
    public void saveData() throws IOException {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonSongs = gson.toJson(songs);
        editor.putString("Data", jsonSongs);
        String jsonStats = gson.toJson(songs);
        editor.putString("Stats", jsonStats);
        String jsonSettings = gson.toJson(settings);
        editor.putString("Settings", jsonSettings);
        editor.apply();
    }
    public void downloadSongInfo(){
        download = new DownloadXmlTask();
        try {
            Log.i(TAG, "songs downloading");
            this.songs = download.execute("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml").get();
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
            downloadsongs = new DownloadSongLyrics();
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

    public UserStatistics getStats() {
        return stats;
    }
    public Settings getSettings(){
        return settings;
    }
}
