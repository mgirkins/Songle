package com.example.maxgirkins.songle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by MaxGirkins on 30/11/2017.
 */

public class completed_songs_list_adapter extends ArrayAdapter {
    private Activity context;
    private String[] titles;
    private String[] artists;
    private String[] youtubeLinks;
    public completed_songs_list_adapter(Activity context,String[] titles, String[] artists, String[] youtubeLinks){
        super(context,R.layout.completed_songs_row, titles);
        this.context = context;
        this.titles = titles;
        this.artists = artists;
        this.context = context;
        this.youtubeLinks = youtubeLinks;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.completed_songs_row, null,true);
        TextView song_title = (TextView) rowView.findViewById(R.id.song_title_completed_listview);
        TextView artist_name = (TextView) rowView.findViewById(R.id.artist_title_completed_listview);
        TextView youtube_links = (TextView) rowView.findViewById(R.id.youtube_link_completed_listview);
        song_title.setText(titles[position]);
        artist_name.setText(artists[position]);
        youtube_links.setText(youtubeLinks[position]);
        return rowView;

    }
}
