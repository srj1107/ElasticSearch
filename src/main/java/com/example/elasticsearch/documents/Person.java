package com.example.elasticsearch.documents;

import com.example.elasticsearch.helper.Indices;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = Indices.PERSON_INDEX) //allow elastic search to create objects of person under person index
public class Person {
    @org.springframework.data.annotation.Id  // to denote the primary key for elastic search index
    @Field(type = FieldType.Keyword)
    private String Id;

    @Field(type = FieldType.Text)
    private String name;

    //getters and setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
