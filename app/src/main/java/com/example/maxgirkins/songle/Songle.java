package com.example.maxgirkins.songle;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by MaxGirkins on 27/11/2017.
 */
//static Singleton class to keep hold of all the data used by different activities
// and handle saving and downloading.
public class Songle extends Application implements DownloadLyricsResponse{
    public static Songle songle;
    private SongList songs;
    private DownloadXmlTask download;
    private DownloadSongLyrics downloadsongs;
    private static  final String PREFS = "PreferencesFile";
    private SharedPreferences sharedPreferences;
    private MainActivity main;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private UserStatistics stats;
    private Settings settings;
    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    public void onCreate(){
        super.onCreate();
        //catch lack of internet before startup 
        if(!receiver.isInternet(getApplicationContext())){
            Intent goNoInternet = new Intent(getApplicationContext(), NoInternetConnection.class);
            goNoInternet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goNoInternet);
        }
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
    //reset user progress
    public void resetProgress(){
        main = new MainActivity();
        songs = new SongList();
        stats = new UserStatistics();
        downloadSongInfo();
        importSongLyrics(songs.getActiveSong().getNum(),songle.getSettings().getDifficulty());
    }
    //load json data from sharedpreferences and make correct objects from the json.
    public void getData(){

        String jsonSongs = sharedPreferences.getString("Songs", "");
        String jsonSettings = sharedPreferences.getString("Settings", "");
        String jsonStats = sharedPreferences.getString("Stats", "");
        UserStatistics stats = gson.fromJson(jsonStats, UserStatistics.class);
        SongList songs = gson.fromJson(jsonSongs, SongList.class);
        Settings settings = gson.fromJson(jsonSettings,Settings.class);
        if (stats != null){
            this.stats = stats;
        }
        if (songs != null){
            this.songs = songs;
        }
        if (settings != null){
            this.settings = settings;
        }

    }
    //save data as json in sharedpreferences file.
    public void saveData() throws IOException {
        //stop distance travelled taking up loads of space.
        stats.removeOldTravels();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonSongs = gson.toJson(songs);
        editor.putString("Songs", jsonSongs);
        String jsonStats = gson.toJson(stats);
        editor.putString("Stats", jsonStats);
        String jsonSettings = gson.toJson(settings);
        editor.putString("Settings", jsonSettings);
        editor.apply();
    }

    public void downloadSongInfo(){
        download = new DownloadXmlTask();
        try {
            SongList temp = download.execute("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml").get();
            //if there are more songs online than on local, add the new songs to local
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
        //make url friendly
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
                this.songs.getSong(num).setLyrics(downloadsongs.execute(strings).get());
                for (int i=0; i<len; i++){
                    if (completed_lyrics.contains(i)){
                        songs.getActiveSong().getLyrics().get(i).setCollected();
                    }
                }
            } else {
                downloadsongs = new DownloadSongLyrics();
                this.songs.getSong(num).setLyrics(downloadsongs.execute(strings).get());
            }

        } catch (InterruptedException | ExecutionException i){
            i.printStackTrace();
        }

    }
    @Override
    public void onLyricsDownloaded(List<Lyric> list) {
        try {
            //calls populate map so map always up to date.
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
