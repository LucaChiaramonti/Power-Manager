package com.powerManager.repository;

import com.powerManager.dto.Test;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository  extends CrudRepository<Test, Long> {
}
