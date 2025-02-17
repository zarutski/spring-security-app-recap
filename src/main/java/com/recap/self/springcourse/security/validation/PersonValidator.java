package com.recap.self.springcourse.security.validation;

import com.recap.self.springcourse.security.model.Person;
import com.recap.self.springcourse.security.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (peopleService.getByUsername(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "Username already registered");
        }
    }
}
