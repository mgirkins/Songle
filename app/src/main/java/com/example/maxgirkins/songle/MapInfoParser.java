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
        /*
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
        return l;*/
        Integer lyricpos = 0;
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
            String name;

            switch (parser.getEventType()){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:

                    name = parser.getName();
                    if (name.equals("name")){
                        lyricpos = readPosition(parser,l);
                    }
                    else if (name.equals("description")){
                        String classification = readClassification(parser);
                        l.get(lyricpos).setClassification(classification,level);
                    } else if (name.equals("Point")){
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
    private  List<Lyric> readPlacemark(XmlPullParser parser, List<Lyric> l) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Document");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
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
                        parser.require(XmlPullParser.START_TAG, ns, "Point");
                        l.get(lyricpos).setCoords(readCoords(parser), level);
                        parser.require(XmlPullParser.END_TAG, ns, "Point");
                    } else {
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
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String pos = readText(parser);
        String integerstring[] = pos.split(":");
        integers[0] = Integer.parseInt(integerstring[0]);
        integers[1] = Integer.parseInt(integerstring[1]);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        for (int i=0; i<l.size();i++){
            if (Arrays.equals(l.get(i).getSongPosition() ,integers)){
                return i;
            }
        }
        return 0;
    }
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
