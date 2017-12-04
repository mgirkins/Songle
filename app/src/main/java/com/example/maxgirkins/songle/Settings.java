package com.example.maxgirkins.songle;

import com.google.gson.annotations.Expose;

/**
 * Created by MaxGirkins on 30/11/2017.
 */
//settings class to handle difficulty and units.
public class Settings {
    @Expose
    private Integer difficulty;
    @Expose
    private String units;

    public Settings(){
        this.difficulty = 0;
        units = "Km";
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
