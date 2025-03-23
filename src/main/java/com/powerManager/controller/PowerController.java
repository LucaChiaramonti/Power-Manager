package com.powerManager.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.powerManager.bin.PowerBin;
import com.powerManager.serializer.TestSerializer;
import com.powerManager.service.PowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/power")
public class PowerController {

    @Autowired
    PowersService powersService;


    @GetMapping("/{level}")
    @JsonSerialize(using = TestSerializer.class)
    public Set<String> importPowers(@PathVariable("level")Integer powerLevel) {
        return powersService.callExternalAPI(powerLevel);

    }

    @GetMapping("/description={description}")
    @JsonSerialize(using = TestSerializer.class)
    public List<PowerBin> getPowersFromDescription(@PathVariable("description")String description) {
        return powersService.getPowersFromDescription(description);

    }

    @GetMapping("/name={name}")
    @JsonSerialize(using = TestSerializer.class)
    public List<PowerBin> getPowersFromName(@PathVariable("name")String name) {
        return powersService.getPowersFromName(name);

    }

}
