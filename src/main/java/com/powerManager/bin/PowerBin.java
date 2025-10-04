package com.powerManager.bin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class PowerBin {
    private String powerName;
    private String powerDescription;
    private List<PowerClassBin> powerClassBins;
    private List<AugmentBin> augmentBins;

    public String getClassLevelsAsString() {
        return powerClassBins.stream()
                .map(PowerClassBin::toString)
                .collect(Collectors.joining("; "));
    }

    public String getFullDescription() {
        String augumentString = augmentBins.stream()
                .map(AugmentBin::toString)
                .collect(Collectors.joining("; "));
        return powerDescription + " " + augumentString;
    }
}
