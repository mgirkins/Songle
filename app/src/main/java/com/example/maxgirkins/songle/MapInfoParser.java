package com.example.maxgirkins.songle;

import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

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

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Document")) {
                l = readPlacemark(parser, l);
            } else {
                skip(parser);
            }
        }
        return l;
    }
    private  List<Lyric> readPlacemark(XmlPullParser parser, List<Lyric> l) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Document");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Placemark")) {
                Integer lyricpos = 0;
                parser.require(XmlPullParser.START_TAG, ns, "Placemark");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG)
                        continue;
                    if (parser.getName().equals("name")){
                        lyricpos = readPosition(parser, l);
                    } else if (parser.getName().equals("description")) {
                         l.get(lyricpos).setClassification(readClassification(parser),level);
                    } else if (parser.getName().equals("Point")){
                        l.get(lyricpos).setCoords(readCoords(parser), level);
                    } else {
                        Log.i(TAG, parser.getName());
                        skip(parser);
                    }
                }
                parser.require(XmlPullParser.END_TAG, ns, "Placemark");
            } else {
                skip(parser);
            }
        }
        return l;
    }

    private Integer readPosition(XmlPullParser parser, List<Lyric> l) throws IOException, XmlPullParserException {
        Integer[] integers = {0,0};
        Integer songIndex = 0;
        Log.i(TAG, "triggered!");
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String pos = readText(parser);
        String integerstring[] = pos.split(":");
        integers[0] = Integer.parseInt(integerstring[0]);
        integers[1] = Integer.parseInt(integerstring[1]);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        for (int i=0; i<l.size();i++){
            if (l.get(i).getSongPosition() == integers){
                return i;
            }
        }
        return 0;
    }
    private LatLng readCoords(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.i(TAG,"Coords called");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();

            if (name.equals("coordinates")) {
                parser.require(XmlPullParser.START_TAG, ns, "coordinates");
                String coords = readText(parser);
                LatLng g = new LatLng(Double.parseDouble(coords.split(",")[0]),Double.parseDouble(coords.split(",")[1]));
                Log.i(TAG,g.toString());
                parser.require(XmlPullParser.END_TAG, ns, "coordinates");
                return g;
            } else {
                skip(parser);
            }
        }
        return null;
    }
    private String readClassification(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        Log.i(TAG,"Description called");
        String classif = readText(parser);
        Log.i(TAG,classif);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return classif;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
