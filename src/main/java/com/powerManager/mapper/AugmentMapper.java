package com.powerManager.mapper;

import com.powerManager.bin.AugmentBin;
import com.powerManager.dto.Augment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AugmentMapper {
    public Augment toDto(AugmentBin powerBin);
    public AugmentBin toBin(Augment powerDto);
}
