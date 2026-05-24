package com.powerManager.mapper;


import com.powerManager.bin.PowerBin;
import com.powerManager.dto.ClassLevel;
import com.powerManager.dto.Power;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PowerMapper {

    @Mapping(source = "powerDto.augments", target = "augmentBins")
    PowerBin toBin(Power powerDto, List<ClassLevel> powerClassBins);


    Power toDto(PowerBin powerBin);
    List<Power> toDto(List<PowerBin> powerBin);
    List<PowerBin> toBin(List<Power> powerBin);

}
