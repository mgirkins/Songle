package com.example.maxgirkins.songle;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class SongTest {
    Song testSong = new Song("bohemian rhapsody", "Queen",0,"https://www.youtube.com/watch?v=fJ9rUzIMcZQ");
    Song testSong2 = new Song("Big Calm", "Morcheeba",1,"https://www.youtube.com/watch?v=fJ9rUzIMcZQ");
    @Before
    public void setup(){
        Lyric l = new Lyric("goodness",new Integer[] {1,1});
        Lyric l2 = new Lyric("gracious",new Integer[] {1,2});
        List<Lyric> lyrics = new ArrayList<>();
        lyrics.add(l);
        lyrics.add(l2);
        testSong.addLyrics(lyrics);
        testSong.getLyrics().get(1).setCollected();
        testSong2.setCompleted();
        testSong2.setDistanceWalked(2100.00);
    }

    @Test
    public void getArtistAndTitle() throws Exception {
        assertEquals(testSong.getArtistAndTitle(), "bohemian rhapsody - Queen");
    }

    @Test
    public void getCompletedLyricsCount() throws Exception {
        assertEquals(testSong.getCompletedLyricsCount(), (Integer) 1);
    }

    @Test
    public void isCompleted() throws Exception {
        assertEquals(testSong2.isCompleted(), true);
    }


}