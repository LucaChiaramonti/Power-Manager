package com.powerManager.dto;

public class PowerSituationRequest {
    private String situation;

    public PowerSituationRequest() {
    }

    public PowerSituationRequest(String situation) {
        this.situation = situation;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}