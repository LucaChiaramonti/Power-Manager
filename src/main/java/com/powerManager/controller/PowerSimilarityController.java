package com.powerManager.controller;

import com.powerManager.dto.PowerSituationRequest;
import com.powerManager.dto.SituationPowerResponse;
import com.powerManager.service.PowerSimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/similarity")
public class PowerSimilarityController {

    private final PowerSimilarityService powerSimilarityService;

    @Autowired
    public PowerSimilarityController(PowerSimilarityService powerSimilarityService) {
        this.powerSimilarityService = powerSimilarityService;
    }

    @GetMapping("/similar/{powerId}")
    public ResponseEntity<List<Long>> findSimilarPowers(
            @PathVariable Long powerId,
            @RequestParam(defaultValue = "100") int maxResults) {

        if (!powerSimilarityService.isInitialized()) {
            return ResponseEntity.status(503).build();
        }

        List<Long> similarPowers = powerSimilarityService.findSimilarPowers(powerId, maxResults);
        return ResponseEntity.ok(similarPowers);
    }

    @PostMapping("/situation")
    public ResponseEntity<List<SituationPowerResponse>> findPowersForSituation(
            @RequestBody PowerSituationRequest request,
            @RequestParam(defaultValue = "100") int maxResults) {

        if (!powerSimilarityService.isInitialized()) {
            return ResponseEntity.status(503).build();
        }

        if (request.getSituation() == null || request.getSituation().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<SituationPowerResponse> powers = powerSimilarityService.findPowersForSituation(
                request.getSituation(), maxResults);
        return ResponseEntity.ok(powers);
    }
}