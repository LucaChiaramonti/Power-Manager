package com.powerManager.repository;

import com.powerManager.dto.ClassLevel;
import com.powerManager.dto.EmbeddedId.ClassLevelId;
import com.powerManager.dto.EmbeddedId.PowerClassId;
import com.powerManager.dto.PowerClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerClassRepository extends JpaRepository<PowerClass, PowerClassId> {
}
