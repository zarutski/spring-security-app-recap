package com.recap.self.springcourse.security.service;

import com.recap.self.springcourse.security.model.Person;
import com.recap.self.springcourse.security.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    
    private static final String ROLE_USER = "ROLE_USER";

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Person> getByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    @Transactional
    public void register(Person person) {
        person.setRole(ROLE_USER);
        person.setPassword(passwordEncoder.encode(person.getPassword())); // should manually encode password, using configured PasswordEncoder
        peopleRepository.save(person);
    }
}
