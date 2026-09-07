package com.norse.hotair.service;

import com.norse.hotair.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class ReadingService {
    private final ReadingRepository readingRepository;

    public ReadingService(ReadingRepository readingRepository) {
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
