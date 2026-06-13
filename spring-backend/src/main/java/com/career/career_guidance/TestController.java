package com.career.career_guidance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Vision Path Backend is Running Successfully";
    }

    @GetMapping("/api/test")
    public String test() {
        return "API is working";
    }
}

