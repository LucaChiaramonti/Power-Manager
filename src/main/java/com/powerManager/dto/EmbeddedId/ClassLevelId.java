package com.powerManager.dto.EmbeddedId;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode

public class ClassLevelId implements Serializable {
    @Column(name = "class_id")
    private long classId;
    @Column(name = "level_id")
    private long levelId;
    public ClassLevelId( Long classId, Long levelId) {
        this.classId = classId;
        this.levelId = levelId;
    }
    public ClassLevelId() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassLevelId classLevelId = (ClassLevelId) o;

        if (this.classId != classLevelId.classId) return false;
        return this.levelId == classLevelId.levelId;
    }

}
