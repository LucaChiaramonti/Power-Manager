package com.powerManager.dto;

import com.powerManager.dto.EmbeddedId.ClassLevelId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ClassLevel {

    @EmbeddedId
    private ClassLevelId id;
    @Transient
    private String className;
    public ClassLevel() {
    }
    public ClassLevel(Class classEntity, Level levelEntity) {
        this.id = new ClassLevelId(classEntity.getIdClass(), levelEntity.getIdLevel());
        className = classEntity.getClassName();
    }
}