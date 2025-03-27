package com.powerManager.service;


import com.powerManager.dto.Level;
import com.powerManager.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LevelService {

    @Autowired
    private LevelRepository levelRepository;

    public List<Level> findAll() {
        return levelRepository.findAll();
    }

    public Optional<Level> findById(Long id) {
        return levelRepository.findById(id);
    }

    public Level save(Level level) {
        return levelRepository.save(level);
    }

    public void deleteById(Long id) {
        levelRepository.deleteById(id);
    }

    public Level update(Long id, Level levelDetails) {
        Optional<Level> optionalLevel = levelRepository.findById(id);
        if (optionalLevel.isPresent()) {
            Level level = optionalLevel.get();
            return levelRepository.save(level);
        } else {
            throw new RuntimeException("Level not found with id " + id);
        }
    }
    public void deleteAll() {
        levelRepository.deleteAll();
    }
}