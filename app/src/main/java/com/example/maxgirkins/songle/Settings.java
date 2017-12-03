package com.example.maxgirkins.songle;

import com.google.gson.annotations.Expose;

/**
 * Created by MaxGirkins on 30/11/2017.
 */

public class Settings {
    @Expose
    private Integer difficulty;
    @Expose
    private Boolean nightMode;
    @Expose
    private String units;

    public Settings(){
        this.difficulty = 0;
        nightMode = false;
        units = "Km";
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getNightMode() {
        return nightMode;
    }

    public void setNightMode(Boolean nightMode) {
        this.nightMode = nightMode;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
