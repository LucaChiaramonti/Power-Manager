package com.powerManager.bin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PowerClassBin {
    private String className;
    private long levelValue;

    @Override
    public String toString() {
        return className + ' ' + levelValue;
    }
}