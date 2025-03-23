package com.powerManager.bin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PowerBin {
    private String powerName;
    private Integer powerLevel;
    private String powerUrl;
    private Integer powerCost;
    private String powerDescription;
    private String powerClass;
    private boolean isAugmentable;
    private List<AugmentBin> augmentBins;

}
