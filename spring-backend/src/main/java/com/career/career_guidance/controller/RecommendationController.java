package com.career.career_guidance.controller;

import com.career.career_guidance.model.User;
import com.career.career_guidance.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RecommendationController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/recommend")
    public String recommendCareer(
            @RequestHeader("Authorization") String token,
            @RequestBody User user) {

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return "Invalid JWT Token";
        }

        String interest = user.getInterest().toLowerCase();
        String skill = user.getSkill().toLowerCase();

        if (interest.contains("networking") && skill.contains("linux")) {
            return "Recommended Career: Cybersecurity Analyst\nRoadmap: Networking -> Linux -> SOC Analyst -> CEH -> Penetration Testing";
        }

        else if (interest.contains("security") && skill.contains("programming")) {
            return "Recommended Career: Secure Software Developer\nRoadmap: Java -> Spring Boot -> OWASP Top 10 -> Secure Coding";
        }

        else if (interest.contains("coding") && skill.contains("java")) {
            return "Recommended Career: Backend Developer\nRoadmap: Java -> Spring Boot -> MySQL -> REST API -> Microservices";
        }

        else if (interest.contains("ai") && skill.contains("python")) {
            return "Recommended Career: Machine Learning Engineer\nRoadmap: Python -> ML Basics -> Data Science -> Deep Learning";
        }

        else if (interest.contains("cloud") && skill.contains("security")) {
            return "Recommended Career: Cloud Security Engineer\nRoadmap: Cloud Basics -> AWS/Azure -> IAM -> Cloud Security";
        }

        else {
            return "Recommended Career: General Software Engineer\nRoadmap: Programming Basics -> Database -> Web Development -> Security Basics";
        }
    }
}
