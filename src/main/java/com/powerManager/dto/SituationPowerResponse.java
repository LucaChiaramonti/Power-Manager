package com.powerManager.dto;

public class SituationPowerResponse {
    private Long idPower;
    private String powerName;
    private String powerDescription;
    private Double relevanceScore;

    public SituationPowerResponse() {
    }

    public SituationPowerResponse(Long idPower, String powerName, String powerDescription, Double relevanceScore) {
        this.idPower = idPower;
        this.powerName = powerName;
        this.powerDescription = powerDescription;
        this.relevanceScore = relevanceScore;
    }

    // Getters and setters
    public Long getIdPower() {
        return idPower;
    }

    public void setIdPower(Long idPower) {
        this.idPower = idPower;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    public String getPowerDescription() {
        return powerDescription;
    }

    public void setPowerDescription(String powerDescription) {
        this.powerDescription = powerDescription;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }
}