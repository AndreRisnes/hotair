package com.norse.hotair.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class TemperatureReadingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS temperature_readings (" +
                "id IDENTITY PRIMARY KEY, " +
                "date_time VARCHAR(255), " +
                "area VARCHAR(255), " +
                "location VARCHAR(255), " +
                "observation DOUBLE)");
    }

    public void registerReading(TemperatureReading reading) {
        jdbcTemplate.update(
                "INSERT INTO temperature_readings (date_time, area, location, observation) VALUES (?, ?, ?, ?)",
                reading.getDateTime(),
                reading.getArea(),
                reading.getLocation(),
                reading.getObservation()
        );
    }
}
