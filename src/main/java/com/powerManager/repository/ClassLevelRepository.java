package com.powerManager.repository;

import com.powerManager.dto.ClassLevel;
import com.powerManager.dto.EmbeddedId.ClassLevelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassLevelRepository extends JpaRepository<ClassLevel, ClassLevelId> {
    @Query("SELECT cl FROM ClassLevel cl " +
            "JOIN Class c ON c.idClass = cl.classEntity.idClass "+
            "JOIN PowerClass pc ON concat(cl.classEntity.idClass, cl.level.idLevel) = pc.id.classLevelId " +
            "JOIN power p ON pc.id.powerId = p.idPower " +
            "WHERE  LOWER(p.powerName) LIKE LOWER(CONCAT('%', :power_name, '%')) ")
    public List<ClassLevel> getClassLevelByPowerName(@Param("power_name") String powerName);

}
