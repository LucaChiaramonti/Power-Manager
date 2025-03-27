package com.powerManager.mapper;

import com.powerManager.bin.PowerClassBin;
import com.powerManager.dto.PowerClass;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassPowerMapper {
    public PowerClass toDto(PowerClassBin powerBin);
    public PowerClassBin toBin(PowerClass powerDto);
}
