package com.example.maxgirkins.songle;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SongLyricParser {
    private static final String TAG = "LyricParserClass";
    private static final String ns = null;
    List<Lyric> parse(InputStream in) throws XmlPullParserException, IOException {
        List<Lyric> l = new ArrayList<>();
        try {
            Log.i(TAG,"Busted");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }
            reader.close();

            String[] lines = out.toString().split("\\n+");
            Log.i(TAG, lines[0]);
            for (int i = 1; i <= lines.length; i++){
                String[] words = lines[i-1].split(" ");
                for (int j = 1; j<=words.length; j++){
                    Integer[] pos = {i,j};
                    Lyric newLyric = new Lyric(words[j-1], pos);
                    l.add(newLyric);
                }
            }
        } finally {
            in.close();
        }
        return l;
    }

}
