package com.example.maxgirkins.songle;

import java.util.List;

/**
 * Created by MaxGirkins on 26/11/2017.
 */

public interface DownloadLyricsResponse {
    void onLyricsDownloaded(List<Lyric> list);
}
