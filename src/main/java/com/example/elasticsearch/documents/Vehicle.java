package com.example.elasticsearch.documents;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;


public class Vehicle {

    //data members associated with class
    private String id;
    private String name;
    private String number;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date created;

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
