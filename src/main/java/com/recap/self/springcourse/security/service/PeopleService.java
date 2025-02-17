package com.recap.self.springcourse.security.service;

import com.recap.self.springcourse.security.model.Person;
import com.recap.self.springcourse.security.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> getByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    @Transactional
    public void register(Person person) {
        peopleRepository.save(person);
    }
}
