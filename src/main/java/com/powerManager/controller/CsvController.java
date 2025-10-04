package com.powerManager.controller;

import com.powerManager.bin.PowerBin;
import com.powerManager.service.CsvExportService;
import com.powerManager.service.PowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/csv")
public class CsvController {
    @Autowired
    private final CsvExportService csvService;
    @Autowired
    PowersService powersService;

    public CsvController(CsvExportService csvService) {
        this.csvService = csvService;
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generateCsv() {

        String filePath = csvService.generateCsvSinglePowers(powersService.getPowersFromName(""));
        return ResponseEntity.ok("CSV generato: " + filePath);
    }
}