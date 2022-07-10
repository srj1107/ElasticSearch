package com.example.elasticsearch.controller;

import com.example.elasticsearch.documents.Vehicle;
import com.example.elasticsearch.search.SearchRequestDTO;
import com.example.elasticsearch.service.VehicleService;
import com.example.elasticsearch.service.helper.VehicleDummyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    //data member
    private VehicleService service;
    private VehicleDummyDataService dummyDataService;

    //constructor
    @Autowired
    public VehicleController(VehicleService service, VehicleDummyDataService dummyDataService){
        this.service = service;
        this.dummyDataService = dummyDataService;
    }

    //post single vehicle
    @PostMapping
    public void index(@RequestBody Vehicle vehicle){
        service.index(vehicle);
    }

    //insert dummy vehicles
    @PostMapping("/insertdummydata")
    public void insertDummyData(){
        dummyDataService.insertDummyData();
    }

//    @PostMapping("/bulk")
//    public void saveAll(@RequestBody List<Vehicle> vehicles){
//        service.saveAll(vehicles);
//    }

    //get a single vehicle on basis of id
    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable String id){
        return service.getById(id);
    }

    //search on basis of field specified in payload
    @PostMapping("/search")
    public List<Vehicle> search(@RequestBody SearchRequestDTO dto){
        return service.search(dto);
    }

    //search all vehicles on basis of payload after the specified date in url path
    @PostMapping("/searchcreatedsince/{date}")
    public List<Vehicle> searchCreatedSince(
            @RequestBody final SearchRequestDTO dto,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            final Date date) {
        return service.searchCreatedSince(dto, date);
    }

    //search all vehicles created after specified date
    @GetMapping("/search/{date}")
    public List<Vehicle> getAllVehiclesCreatedSince(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            final Date date
    ){
        return service.getAllVehiclesCreatedSince(date);
    }

//    @GetMapping("/{number}")
//    public List<Vehicle> findByNumber(@PathVariable String number){
//        return service.findByNumber(number);
//    }
}
