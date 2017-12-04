package com.example.maxgirkins.songle;

import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */
//class to parse the kml data about a song to create lyric objects.
public class MapInfoParser {
    private static final String TAG = "MapParserClass";
    private static final String ns = null;
    private List<Lyric> lyrics = new ArrayList<Lyric>();
    private  Integer level;
    public MapInfoParser(List<Lyric> l, Integer level){
        this.lyrics = l;
        this.level = level;
    }
    public List<Lyric> parse(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                    false);
            parser.setInput(in, null);
            parser.nextTag();
            lyrics = readFeed(parser, lyrics);
            return lyrics;
        } finally {
            in.close();
        }
    }
    private List<Lyric> readFeed(XmlPullParser parser, List<Lyric> l) throws XmlPullParserException, IOException {
        Integer lyricpos = 0;
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
            String name;

            switch (parser.getEventType()){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:

                    name = parser.getName();
                    //get song positions
                    if (name.equals("name")){
                        lyricpos = readPosition(parser,l);
                    }
                    //get classification
                    else if (name.equals("description")){
                        String classification = readClassification(parser);
                        l.get(lyricpos).setClassification(classification,level);
                    }
                    // get coordinates
                    else if (name.equals("Point")){
                        l.get(lyricpos).setCoords(readCoords(parser),level);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                default:
                    break;
            }
            parser.next();
        }
        return l;
    }

    //reads the position of the lyric in the song
    private Integer readPosition(XmlPullParser parser, List<Lyric> l) throws IOException, XmlPullParserException {
        Integer[] integers = {0,0};
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String pos = readText(parser);
        String integerstring[] = pos.split(":");
        integers[0] = Integer.parseInt(integerstring[0]);
        integers[1] = Integer.parseInt(integerstring[1]);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        //if parsed song position matches song position of raw lyric then get index of that lyric for later.
        for (int i=0; i<l.size();i++){
            if (Arrays.equals(l.get(i).getSongPosition() ,integers)){
                return i;
            }
        }
        return 0;
    }
    //get coordinates of the lyric
    private LatLng readCoords(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            parser.require(XmlPullParser.START_TAG, ns, "coordinates");
            String coords = readText(parser);
            LatLng g = new LatLng(Double.parseDouble(coords.split(",")[1]),Double.parseDouble(coords.split(",")[0]));
            parser.require(XmlPullParser.END_TAG, ns, "coordinates");
            return g;
        }
        return null;
    }

    private String readClassification(XmlPullParser parser) throws IOException, XmlPullParserException {
        String s = readText(parser);
        return s;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
