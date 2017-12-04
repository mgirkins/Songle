package com.example.maxgirkins.songle;


import org.xmlpull.v1.XmlPullParserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//parse the raw text of the songs to List<Lyric>
public class SongLyricParser {
    private static final String TAG = "LyricParserClass";
    private static final String ns = null;
    List<Lyric> parse(InputStream in) throws XmlPullParserException, IOException {
        List<Lyric> l = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            //if the end of the line has been reached, add newline
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }
            reader.close();
            //split song by line
            String[] lines = out.toString().split("\\n+");
            //start loop at 1 so line 1 is 1 not line 0
            for (int i = 1; i <= lines.length; i++){
                String[] words = lines[i-1].split(" ");
                //start loop at 1 so lyric 1 is 1 not lyric 0
                for (int j = 1; j<=words.length; j++){
                    Integer[] pos = {i,j};
                    String w;
                    //if end of line word is word, add newline
                    if (j==words.length){
                        w = words[j-1] + "\n";

                    }
                    else {
                        w = words[j-1];
                    }
                    //create lyric from word and position
                    Lyric newLyric = new Lyric(w, pos);
                    l.add(newLyric);

                }
            }
        } finally {
            in.close();
        }
        return l;
    }

}
