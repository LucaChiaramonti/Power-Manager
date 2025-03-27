package com.powerManager.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.powerManager.bin.PowerBin;
import com.powerManager.serializer.TestSerializer;
import com.powerManager.service.PowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/power")
public class PowerController {

    @Autowired
    PowersService powersService;


    @PostMapping()
    @JsonSerialize(using = TestSerializer.class)
    public Set<String> importPowers() {
        return powersService.readMainPage();

    }

    @GetMapping("/description={description}")
    @JsonSerialize(using = TestSerializer.class)
    public List<PowerBin> getPowersFromDescription(@PathVariable("description")String description) {
        return powersService.getPowersFromDescription(description);

    }

    @GetMapping("/name={name}")
    public List<PowerBin> getPowersFromName(@PathVariable("name")String name) {
        List<PowerBin> powerBins = powersService.getPowersFromName(name);
        return powerBins;

    }

    @DeleteMapping
    @JsonSerialize(using = TestSerializer.class)
    public void deleteAll() {
        powersService.deleteAll();
    }

}
