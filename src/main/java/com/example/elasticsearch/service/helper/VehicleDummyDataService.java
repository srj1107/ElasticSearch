package com.example.elasticsearch.service.helper;

import com.example.elasticsearch.documents.Vehicle;
import com.example.elasticsearch.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
public class VehicleDummyDataService {
    private static final Logger LOG = LoggerFactory.getLogger(VehicleDummyDataService.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final VehicleService service;

    public VehicleDummyDataService(final VehicleService service){
        this.service = service;
    }

    public void insertDummyData(){
//        service.index(buildVehicle("100", "Audi A1", "AAA-123", "2010-01-01"));
//        service.index(buildVehicle("200", "Audi A3", "AAB-123", "2011-07-05"));
//        service.index(buildVehicle("300", "Audi A3", "AAC-123", "2012-10-03"));
//
//        service.index(buildVehicle("400", "BMW M3", "AAA-023", "2021-10-06"));
        service.index(buildVehicle("500", "BMW 3", "1AA-023", "2001-10-01"));
        service.index(buildVehicle("600", "BMW M5", "12A-023", "1999-05-08"));

        service.index(buildVehicle("700", "VW Golf", "42A-023", "1991-04-08"));
        service.index(buildVehicle("800", "VW Passat", "18A-023", "2021-04-08"));

        service.index(buildVehicle("900", "Skoda Kodiaq", "28A-023", "2020-01-04"));
        service.index(buildVehicle("1000", "Skoda Yeti", "88A-023", "2015-03-09"));
    }

    private static Vehicle buildVehicle(
            final String id,
            final String name,
            final String number,
            final String date
    ){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setName(name);
        vehicle.setNumber(number);
        try{
            vehicle.setCreated(DATE_FORMAT.parse(date));
        }catch(ParseException e){
            LOG.error(e.getMessage(), e);
        }
        return vehicle;
    }
}
