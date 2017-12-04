package com.example.maxgirkins.songle;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by MaxGirkins on 04/12/2017.
 */
public class SongListTest {
    SongList songs = new SongList();
    Song testSong = new Song("bohemian rhapsody", "Queen",0,"https://www.youtube.com/watch?v=fJ9rUzIMcZQ");
    Song testSong2 = new Song("Big Calm", "Morcheeba",1,"https://www.youtube.com/watch?v=fJ9rUzIMcZQ");
    @Before
    public void setUp() throws Exception {
        songs = new SongList();

        songs.addSong(testSong);
        songs.addSong(testSong2);
        songs.getSong(0).setCompleted();
    }

    @Test
    public void getSong() throws Exception {
        assertEquals(songs.getSong(0),testSong);
    }


    @Test
    public void getCompletedSongsCount() throws Exception {

        assertEquals(songs.getCompletedSongsCount(),(Integer) 1);
    }

    @Test
    public void getTitlesAndArtist() throws Exception {
        List<String> test = new ArrayList<>(Arrays.asList("bohemian rhapsody - Queen","Big Calm - Morcheeba"));
        assertEquals(test, songs.getTitlesAndArtist());
    }

    @Test
    public void getCompletedTitles() throws Exception {
        List<String> test = new ArrayList<>(Arrays.asList("bohemian rhapsody"));
        assertEquals(test, songs.getCompletedTitles());
    }

    @Test
    public void getCompletedArtists() throws Exception {
        List<String> test = new ArrayList<>(Arrays.asList("Queen"));
        assertEquals(test, songs.getCompletedArtists());
    }

    @Test
    public void getNumSongs() throws Exception {
        assertEquals((Integer)2,songs.getNumSongs());
    }

    @Test
    public void addSong() throws Exception {
        Song testSong3 = new Song("never gonna give you up", "Rick Astley",1,"https://www.youtube.com/watch?v=fJ9rUzIMcZQ");
        songs.addSong(testSong3);
        assertEquals((Integer) 3, songs.getNumSongs());
    }

}