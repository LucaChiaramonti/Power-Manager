package com.powerManager.service;

import com.powerManager.bin.TestBin;
import com.powerManager.dto.Test;
import com.powerManager.mapper.TestMapper;
import com.powerManager.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestMapper testMapper;




    public Optional<Test> getTest(Long id) {
        return testRepository.findById(id);
    }
    public TestBin saveTest(TestBin test) {
        return testMapper.toBin(testRepository.save(testMapper.toDto(test)));
    }
    @Transactional
    public void deleteTest(Long id){


        testRepository.deleteById(id);

    }
}
