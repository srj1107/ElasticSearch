package com.example.elasticsearch.controller;

import com.example.elasticsearch.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index")
public class IndexController {
    private final IndexService service;

    //inject service bean
    @Autowired
    public IndexController(IndexService service){
        this.service = service;
    }

    //recrete indices
    @PostMapping("/recreate")
    public void recreateAllIndices(){
        service.recreateIndices(true);
    }
}
