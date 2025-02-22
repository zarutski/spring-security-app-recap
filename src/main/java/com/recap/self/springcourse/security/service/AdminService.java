package com.recap.self.springcourse.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')") // --- method-level restriction for authorization [can be used as configuration-based authorization replacement/extension]
    public void doAminStuff() {
        System.out.println("Only admin here");
    }
}
