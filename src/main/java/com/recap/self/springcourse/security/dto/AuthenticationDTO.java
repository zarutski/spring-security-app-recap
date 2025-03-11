package com.recap.self.springcourse.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AuthenticationDTO {

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    private String username;

    private String password;

    public AuthenticationDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
