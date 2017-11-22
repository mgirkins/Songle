package com.example.maxgirkins.songle;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by MaxGirkins on 22/11/2017.
 */

public class XmlParser {
    private static final String TAG = "ParserClass";
    private static final String ns = null;
    SongList parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                    false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.i(TAG, readFeed(parser).getNumSongs().toString());
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    private SongList readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Songs");
        SongList songs = new SongList();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the song tag
            if (name.equals("Song")) {
                songs.addSong(readSong(parser));
            } else {
                skip(parser);
            }
        }
        return songs;
    }
    private Song readSong(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Song");
        String title = null; String artist = null; Integer number = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            continue;
            String name = parser.getName();
            if (name.equals("Title")) {
                title = readTitle(parser);
            } else if (name.equals("Artist")) {
                artist = readArtist(parser);
            } else if (name.equals("Number")) {
                number = readNumber(parser);
            } else {
                skip(parser);
            }
        }
        return new Song(title, artist, number);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Title");
        return title;
    }
    private Integer readNumber(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Number");
        String num = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Number");
        return Integer.parseInt(num);
    }
    private String readArtist(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Artist");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Artist");
        return summary;
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
