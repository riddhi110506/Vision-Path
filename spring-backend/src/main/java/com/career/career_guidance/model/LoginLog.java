package com.career.career_guidance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String status;
    private LocalDateTime loginTime;

    public LoginLog() {
    }

    public LoginLog(String username, String status, LocalDateTime loginTime) {
        this.username = username;
        this.status = status;
        this.loginTime = loginTime;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
}

