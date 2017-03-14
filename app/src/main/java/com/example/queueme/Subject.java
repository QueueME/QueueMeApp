package com.example.queueme;

/**
 * Created by anders on 14.02.2017.
 */

public class Subject {
    private String emnekode;
    private String emnenavn;

    private void Subject(){

    }

    public String getEmnenavn() {
        return emnenavn;
    }

    public void setEmnenavn(String emnenavn) {
        this.emnenavn = emnenavn;
    }

    public String getEmnekode() {
        return emnekode;
    }

    public void setEmnekode(String emnekode) {
        this.emnekode = emnekode;
    }

    @Override
    public String toString() {
        return "" + emnekode +" "+ emnenavn;
    }
}
