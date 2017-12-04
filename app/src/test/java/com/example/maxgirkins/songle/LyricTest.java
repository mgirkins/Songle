package com.example.maxgirkins.songle;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by MaxGirkins on 04/12/2017.
 */
public class LyricTest {
    Lyric l1 = new Lyric("Hello", new Integer[] {1,1});
    Lyric l2 = new Lyric("world", new Integer[] {1,2});
    @Before
    public void setUp() throws Exception {
        l1.setClassification("boring", 3);
        l2.setCollected();
        l2.setClassification("veryInteresting", 0);
    }

    @Test
    public void getLyric() throws Exception {
        assertEquals("Hello", l1.getLyric());
    }
    @Test
    public void setCoords() throws Exception {
        LatLng test = new LatLng(3.00112, 54.00345);
        l1.setCoords(new LatLng(3.00112, 54.00345),3);
        assertEquals(l1.getCoords(3),test);
    }

    @Test
    public void setClassification() throws Exception {
        l1.setClassification("boring", 0);
        assertEquals("boring",l1.getClassification(0));
    }

    @Test
    public void getClassification() throws Exception {
        assertEquals("veryInteresting", l2.getClassification(0));
    }

    @Test
    public void isCollected() throws Exception {
        assertEquals(true,l2.isCollected());
    }

    @Test
    public void getSongPosition() throws Exception {
        assertArrayEquals(new Integer[] {1,2},l2.getSongPosition());
    }


    @Test
    public void toCensoredString() throws Exception {
        String test = new StringBuilder().append('\u25A0').append('\u25A0').append('\u25A0')
                .append('\u25A0').append('\u25A0').toString();
        assertEquals(test, l1.toCensoredString());
    }

}