package com.powerManager.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.powerManager.bin.TestBin;
import com.powerManager.dto.Test;
import com.powerManager.serializer.TestSerializer;
import com.powerManager.exception.TestNotFoundException;
import com.powerManager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class TestController {

    @Autowired
    TestService testService;


    @PostMapping("/test")
    @ResponseBody
    public TestBin addTest(@RequestBody TestBin testBin) {
        return testService.saveTest(testBin);
    }

    @GetMapping("/test/{id}")
    @JsonSerialize(using = TestSerializer.class)
    public ResponseEntity<Test> getTest(@PathVariable("id") Long id) {
        Optional<Test> test = testService.getTest(id);
        return test.map(value -> new ResponseEntity<Test>(
                value,
                HttpStatus.OK)).orElseThrow(() -> new TestNotFoundException("Test with id:" + id + " not found"));
    }

    @DeleteMapping("/test/{id}")
    public String deleteTest(@PathVariable("id")Long id) {
          testService.deleteTest(id);

        return "OK";    }
}
