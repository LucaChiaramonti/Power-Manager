package com.powerManager.dto;

import com.powerManager.dto.EmbeddedId.ClassLevelId;
import com.powerManager.dto.EmbeddedId.PowerClassId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PowerClass {
    @EmbeddedId
    private PowerClassId id;
    @Transient
    private String className;
    @Transient
    private long level;

    @ManyToOne
    @MapsId("powerId")
    @JoinColumn(name="power_id", nullable=false)
    private Power power;
    public PowerClass() {
    }
    public PowerClass(ClassLevel classEntity, Power powerEntity) {
        this.id = new PowerClassId(powerEntity.getIdPower(), classEntity.getId().toString());
        level = classEntity.getId().getLevelId();
        className = classEntity.getClassName();
        power = powerEntity;
    }
}



