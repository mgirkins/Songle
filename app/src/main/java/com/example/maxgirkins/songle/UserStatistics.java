package com.example.maxgirkins.songle;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Date;

import static com.example.maxgirkins.songle.Songle.songle;

/**
 * Created by MaxGirkins on 29/11/2017.
 */

//class to handle the user's stats
public class UserStatistics {
        //expose to Gson
        @Expose
        private Double totalDistance;
        @Expose
        private ArrayList<Double> travels;
        @Expose
        //arraylist of times used to give total distance in the last day/week/etc.
        private ArrayList<Date> travel_times;

        public UserStatistics(){
            totalDistance = 0.0;
            travels = new ArrayList<>();
            travel_times = new ArrayList<>();
        }
    //return total distance in miles or kilometers depending on user settings
    public Double getTotalDistance() {
        if (songle.getSettings().getUnits().equals("Km")){
            return totalDistance/1000;
        } else {
            return (0.621371*(totalDistance/1000));
        }

    }

    public void addToTotalDistance(double distance) {
        this.totalDistance += distance;
    }
    //return total travel since date given in miles or kilometers depending on settings.
    public Double getTravelDistanceInRange(Date daterange) {
        Double total = 0.0;
        for (int i=0; i<travels.size(); i++){
            if (travel_times.get(i).after(daterange)){
                total += travels.get(i);
            }
        }
        if (songle.getSettings().getUnits().equals("Km")){
            return total/1000;
        } else {
            return (0.621371*(total/1000));
        }
    }
    //add new travel data with time
    public void addTravels(Double travels, Date timestamp) {
        this.travels.add(travels);
        this.travel_times.add(timestamp);
    }
    //remove travel data older than 8 days to save space when storing data.
    public void removeOldTravels(){
        Date date = new Date(System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000));
        for (int i=0; i<travel_times.size();i++){
            if (travel_times.get(i).before(date)){
                travel_times.remove(i);
                travels.remove(i);
            }
        }
    }
}
