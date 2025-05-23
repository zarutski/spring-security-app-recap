package com.recap.self.springcourse.security.controller;


import com.recap.self.springcourse.security.model.Person;
import com.recap.self.springcourse.security.secure.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/show-user-info")
    public String userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) authentication.getPrincipal();
        Person person = details.getPerson();
        System.out.println("Simple logging: " + person);
        return "hello";
    }
}
