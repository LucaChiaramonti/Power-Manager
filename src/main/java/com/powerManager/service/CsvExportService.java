package com.powerManager.service;

import com.powerManager.bin.PowerBin;
import com.powerManager.bin.PowerClassBin;
import com.powerManager.dto.Power;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CsvExportService {
    public String generateCsv(List<PowerBin> powers) {
        String filePath = "powerDataset.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Power_Name;Description;Class;Level\n");
            for (PowerBin power : powers) {
                List<PowerClassBin> powerClassBins = power.getPowerClassBins();
                for (PowerClassBin powerClassBin : powerClassBins) {
                    writer.append(power.getPowerName().replaceAll(";","")).append(" ; ")
                        .append(String.valueOf(power.getFullDescription()).replaceAll(";","")).append(" ; ")
                        .append(powerClassBin.getClassName().replaceAll(";","")).append(" ; ")
                        .append(String.valueOf(powerClassBin.getLevelValue()).replaceAll(";","")).append("\n");
                }
            }
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Errore nella generazione del CSV";
        }
    }

    public String generateCsvSinglePowers(List<PowerBin> powers) {
        String filePath = "noDuplicatesPowerDataset.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Power_Name;Description\n");
            for (PowerBin power : powers) {
                List<PowerClassBin> powerClassBins = power.getPowerClassBins();
                    writer.append(power.getPowerName().replaceAll(";","")).append(" ; ")
                            .append(String.valueOf(power.getFullDescription()).replaceAll(";","")).append("\n");

            }
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Errore nella generazione del CSV";
        }
    }


}
