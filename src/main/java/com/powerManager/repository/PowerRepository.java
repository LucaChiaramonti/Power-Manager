package com.powerManager.repository;

import com.powerManager.dto.Power;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PowerRepository extends CrudRepository<Power, Long> {
    @Query("from power WHERE LOWER(power_description) LIKE LOWER(CONCAT('%', :description, '%'))")
    public List<Power> findPowerFromDescription(@Param("description") String description);

    @Query("from power WHERE LOWER(power_name) LIKE LOWER(CONCAT('%', :powerName, '%'))")
    public List<Power> findPowerFromName(@Param("powerName") String powerName);

}
