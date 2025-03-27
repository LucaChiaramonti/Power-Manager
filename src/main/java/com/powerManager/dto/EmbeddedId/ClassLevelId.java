package com.powerManager.dto.EmbeddedId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
@Getter
@Setter
public class ClassLevelId implements Serializable {
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "level_id")
    private Long levelId;

    public ClassLevelId() {
    }

    public ClassLevelId(Long classId, Long levelId) {
        this.classId = classId;
        this.levelId = levelId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassLevelId classLevelId = (ClassLevelId) o;

        if (this.classId != classLevelId.classId) return false;
        return this.levelId == classLevelId.levelId;
    }
    public String toString() {
        return classId + "" + levelId;
    }

}
