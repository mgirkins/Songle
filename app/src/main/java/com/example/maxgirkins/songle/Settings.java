package com.example.maxgirkins.songle;

/**
 * Created by MaxGirkins on 30/11/2017.
 */

public class Settings {
    private Integer difficulty = 4;
    private Boolean nightMode;
    private String units;

    public Settings(){
        this.difficulty = 4;
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
