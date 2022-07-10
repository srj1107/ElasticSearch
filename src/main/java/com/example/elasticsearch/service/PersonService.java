package com.example.elasticsearch.service;

import com.example.elasticsearch.documents.Person;
import com.example.elasticsearch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository){
        this.repository = repository;
    }

    public void save(Person person){
        repository.save(person);
    }


    public Person findById(String id){
        return repository.findById(id).orElse(null);
    }



}
