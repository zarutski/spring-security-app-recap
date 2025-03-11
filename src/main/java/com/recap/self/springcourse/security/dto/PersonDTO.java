package com.recap.self.springcourse.security.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    private String username;

    @Min(value = 1900, message = "Year of birth should be at least 1900")
    @Max(value = 2025, message = "Year of birth should not greater than 2025")
    private int yearOfBirth;

    private String password;

    public PersonDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
