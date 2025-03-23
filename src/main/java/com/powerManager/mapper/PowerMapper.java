package com.powerManager.mapper;

import com.powerManager.bin.PowerBin;
import com.powerManager.dto.Augment;
import com.powerManager.dto.Power;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PowerMapper {
    public Power toDto(PowerBin powerBin);
    @Mapping(source = "augmentDtos", target = "augmentBins")
    PowerBin toBin(Power powerDto, List<Augment> augmentDtos);
    public List<Power> toDto(List<PowerBin> powerBin);
    public List<PowerBin> toBin(List<Power> powerBin);



}
