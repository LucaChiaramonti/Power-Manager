package com.powerManager.repository;

import com.powerManager.dto.ClassLevel;
import com.powerManager.dto.EmbeddedId.ClassLevelId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassLevelRepository extends JpaRepository<ClassLevel, ClassLevelId> {

}
