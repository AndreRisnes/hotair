package com.norse.hotair.service;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TemperatureReading {

    private String dateTime;
    private String area;
    private String location;
    private double observation;

    public TemperatureReading(String dateTime, String area, String location, double observation) {
        this.dateTime = dateTime;
        this.area = area;
        this.location = location;
        this.observation = observation;
    }

    public TemperatureReading(String csvLine) {
        var fields = csvLine.split(",");
        this.dateTime = fields[0];
        this.area = fields[1];
        this.location = fields[2];
        this.observation = Double.parseDouble(fields[3]);
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getArea() {
        return area;
    }

    public String getLocation() {
        return location;
    }

    public double getObservation() {
        return observation;
    }

    public static List<TemperatureReading> loadFrom(ClassPathResource resource) {
        var result = new ArrayList<TemperatureReading>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (!first) {  // skip header line
                    result.add(new TemperatureReading(line));
                }
                first = false;
            }
            return result;
        } catch (IOException readError) {
            throw new UncheckedIOException(readError);
        }
    }

    public boolean isFrom(String area) {
        return this.area == area;
    }
}
