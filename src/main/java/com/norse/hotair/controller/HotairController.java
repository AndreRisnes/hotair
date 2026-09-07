package com.norse.hotair.controller;

import com.norse.hotair.service.HotairService;
import com.norse.hotair.service.TemperatureReading;
import com.norse.hotair.service.TemperatureReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HotairController {
    private static final String DATA_DIR = "src/main/resources/data";

    @Autowired
    private HotairService hotairService;

    @Autowired
    private TemperatureReadingService temperatureReadingService;

    @GetMapping("/average/{area}")
    public Map<String, Object> getAverageTemperature(@PathVariable String area) {
        double average = hotairService.getAverageTemperature(area);

        Map<String, Object> response = new HashMap<>();
        response.put("area", area);
        response.put("average", average);
        return response;
    }

    @PostMapping("/history/download")
    public ResponseEntity<FileSystemResource> downloadHistory(@RequestBody String stationFile) {
        File file = new File(DATA_DIR + "/" + stationFile);
        FileSystemResource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + stationFile)
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(resource);
    }

    @PostMapping("/reading")
    public void registerReading(@RequestBody TemperatureReading reading) {
        temperatureReadingService.registerReading(reading);
    }
}
