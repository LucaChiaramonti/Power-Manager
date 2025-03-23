package com.powerManager.mapper;

import com.powerManager.bin.TestBin;
import com.powerManager.dto.Test;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    public Test toDto(TestBin testBin);
    public TestBin toBin(Test testDto);

}
