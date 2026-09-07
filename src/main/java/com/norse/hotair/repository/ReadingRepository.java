package com.norse.hotair.repository;

import com.norse.hotair.service.TemperatureReading;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@SuppressWarnings({"SqlSourceToSinkFlow", "resource", "SqlNoDataSourceInspection"})
@Repository
public class ReadingRepository {
    private static final String DB_URL = "jdbc:h2:file:./data/hotair";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static final String[] HISTORIC_DATA_FILES = {
        "station-north.csv",
        "station-south.csv"
    };

    public List<TemperatureReading> loadHistoricReadings(String area) {
        return Arrays.stream(HISTORIC_DATA_FILES)
                .map(file -> new ClassPathResource("historicdata/" + file))
                .map(TemperatureReading::loadFrom)
                .flatMap(List::stream)
                .filter(reading -> reading.isFrom(area))
                .collect(toCollection(ArrayList::new));
    }

    @PostConstruct
    public void init() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.createStatement().execute("""
                create table if not exists temperature_readings (
                    id identity primary key,
                    date_time timestamp,
                    area varchar(128) not null,
                    location VARCHAR(128) not null,
                    observation double not null
                )
                """);
    }

    public void registerReading(TemperatureReading reading) throws SQLException {
        String sql = "insert into temperature_readings (date_time, area, location, observation) values ("
                + "'"  + reading.getDateTime() + "',''" + reading.getArea() + "', '" + reading.getLocation() + "', " + reading.getObservation() + ")";

        DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD).prepareStatement(sql).execute();
    }

    public List<TemperatureReading> loadCurrentReadings(String area) throws SQLException {
        String sql = "select date_time, area, location, observation from temperature_readings where area = '" + area + "'";

        ResultSet rs = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD).createStatement().executeQuery(sql);

        List<TemperatureReading> readings = new ArrayList<>();
        while (rs.next()) {
            TemperatureReading reading = new TemperatureReading(
                rs.getString("date_time"),
                rs.getString("area"),
                rs.getString("location"),
                rs.getDouble("observation")
            );
            readings.add(reading);
        }

        return readings;
    }
}
