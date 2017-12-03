package com.example.maxgirkins.songle;

import com.google.gson.annotations.Expose;

/**
 * Created by MaxGirkins on 29/11/2017.
 */

public class UserStatistics {
        @Expose
        private double totalDistance;

        public UserStatistics(){
            totalDistance = 0.0;
        }

    public double getTotalDistance() {
        return totalDistance/1000;
    }

    public void addToTotalDistance(double distance) {
        this.totalDistance += distance;
    }
}
