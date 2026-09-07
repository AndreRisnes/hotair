package com.norse.hotair.service;

public class TemperatureReading {

    private String dateTime;
    private String area;
    private String location;
    private double observation;

    public TemperatureReading() {
    }

    public TemperatureReading(String dateTime, String area, String location, double observation) {
        this.dateTime = dateTime;
        this.area = area;
        this.location = location;
        this.observation = observation;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getObservation() {
        return observation;
    }

    public void setObservation(double observation) {
        this.observation = observation;
    }
}
