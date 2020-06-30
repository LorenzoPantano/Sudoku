package it.daloma.sudoku.utils;

import java.util.ArrayList;

public class Times {

    private ArrayList<Long> times = new ArrayList<>();

    public Times(ArrayList<Long> times) {
        this.times = times;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Long> times) {
        this.times = times;
    }
}
