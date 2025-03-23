package com.powerManager.repository;

import com.powerManager.dto.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Long> {

    public Class findClassByClassName(String className);
}