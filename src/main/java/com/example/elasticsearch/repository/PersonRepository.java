package com.example.elasticsearch.repository;


import com.example.elasticsearch.documents.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {
}
