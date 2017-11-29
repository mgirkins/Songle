package com.example.maxgirkins.songle;

/**
 * Created by MaxGirkins on 29/11/2017.
 */

public class UserStatistics {
        private double totalDistance;
        public UserStatistics(){
            totalDistance = 0.0;
        }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double distance) {
        this.totalDistance += distance;
    }
}
