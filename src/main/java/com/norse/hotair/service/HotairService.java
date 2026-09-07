package com.norse.hotair.service;

import com.norse.hotair.repository.ReadingRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toCollection;


@Service
public class HotairService {
    private final ReadingRepository readingRepository;

    public HotairService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public void registerReading(TemperatureReading reading) throws Exception {
        readingRepository.registerReading(reading);
    }

    public double getAverageTemperature(String area) throws SQLException {
        List<TemperatureReading> readings = readingRepository.loadHistoricReadings(area);
        readingRepository.loadReadings(area).forEach(reading -> {
            if (!readings.contains(reading)) {
                readings.add(reading);
            }
        });

        return readings.stream().mapToDouble(TemperatureReading::getObservation).average().orElse(0f);
    }

}
