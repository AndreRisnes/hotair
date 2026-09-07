package com.norse.hotair.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class HotairService {

    // Known stations. New stations need to be added here as well as dropping
    // the csv file into resources/data.
    private static final String[] STATION_FILES = {
            "station-north.csv",
            "station-south.csv"
    };

    public List<TemperatureReading> loadAllReadings() {
        List<TemperatureReading> readings = new ArrayList<>();

        for (String fileName : STATION_FILES) {
            readings.addAll(readStationFile(fileName));
        }

        return readings;
    }

    private List<TemperatureReading> readStationFile(String fileName) {
        List<TemperatureReading> readings = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource("data/" + fileName);
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    // skip header row: datetime,area,location,observation
                    firstLine = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",");
                String dateTime = parts[0];
                String area = parts[1];
                String location = parts[2];
                double observation = Double.parseDouble(parts[3]);

                readings.add(new TemperatureReading(dateTime, area, location, observation));
            }
        } catch (IOException e) {
            System.out.println("Could not read station file " + fileName + ": " + e.getMessage());
        }

        return readings;
    }

    public double getAverageTemperature(String area) {
        List<TemperatureReading> all = loadAllReadings();

        double sum = 0;
        int count = 0;
        for (TemperatureReading reading : all) {
            if (reading.getArea().equalsIgnoreCase(area)) {
                sum += reading.getObservation();
                count++;
            }
        }

        return sum / count;
    }
}
