package com.powerManager.bin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class PowerBin {
    private String powerName;
    private String powerDescription;
    private List<PowerClassBin> powerClassBins;
    private List<AugmentBin> augmentBins;


}
