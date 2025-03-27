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
public class ClassService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestMapper testMapper;



    @Transactional
    public void deleteAll(){


    }
}
