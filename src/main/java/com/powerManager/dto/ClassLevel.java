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
    @Transient
    private Long levelValue;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private Class classEntity;

    @ManyToOne
    @MapsId("levelId")
    @JoinColumn(name = "level_id")
    private Level level;

    public ClassLevel() {

    }

    public ClassLevel(Class classEntity, Level levelEntity) {
        this.id = new ClassLevelId(classEntity.getIdClass(), levelEntity.getIdLevel());
        this.classEntity = classEntity;
        this.level = levelEntity;
        this.levelValue = levelEntity.getIdLevel();
        className = classEntity.getClassName();
    }

    public String getStringId() {
        return className + " " + id.getLevelId();
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void updateClassName() {
        this.className = classEntity.getClassName();
        this.levelValue = level.getIdLevel();
    }

}