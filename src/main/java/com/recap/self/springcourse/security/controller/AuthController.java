package com.recap.self.springcourse.security.controller;

import com.recap.self.springcourse.security.dto.AuthenticationDTO;
import com.recap.self.springcourse.security.dto.PersonDTO;
import com.recap.self.springcourse.security.model.Person;
import com.recap.self.springcourse.security.secure.JWTUtil;
import com.recap.self.springcourse.security.service.PeopleService;
import com.recap.self.springcourse.security.validation.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager; // inject DAO authentication manager [pre-configured bean for DAO based credentials check]

    @Autowired
    public AuthController(PersonValidator personValidator, PeopleService peopleService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }


    // method for login and retrieve new jwt token [in case old jwt expired]
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(),
                authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException ex) {
            return Map.of("message", "Authentication credential error"); // --- temporary stub (better replace with Exception throw)
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/registration")
    public Map<String, String> registerPerson(@RequestBody @Valid PersonDTO personDTO,
                                              BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("message", "Registration validation error"); // --- temporary stub (better replace with Exception throw)
        }
        peopleService.register(person);
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token", token);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
