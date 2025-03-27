package com.powerManager.dto.EmbeddedId;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
public class PowerClassId implements Serializable {

    @Column(name = "power_id")
    private long powerId;
    @Column(name = "class_level_id")
    private String classLevelId;
    public PowerClassId( Long powerId, String classLevelId) {
        this.powerId = powerId;
        this.classLevelId = classLevelId;
    }
    public PowerClassId() {
    }
    @Override
    public String toString() {
        return "PowerClassId{" +
                "powerId=" + powerId +
                ", classLevelId=" + classLevelId +
                '}';
    }

}
