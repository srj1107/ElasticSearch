package com.example.elasticsearch.service;

import com.example.elasticsearch.documents.Vehicle;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.search.SearchRequestDTO;
import com.example.elasticsearch.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//Bussiness class vehicle logic
@Service
public class VehicleService {

    //mapper is used to create mappings for an object
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //logger for Vehicle Service
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    //elastic search client
    private final RestHighLevelClient client;

    //injecting client bean
    @Autowired
    public VehicleService(RestHighLevelClient client){
        this.client = client;
    }

    //search on basis of payload
    public List<Vehicle> search(final SearchRequestDTO dto){
        final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX, dto); //returns filtered data

        //returns list of vehicles mapped in json format
        return searchInternal(request);
    }

    //search on basis of time range query
    public List<Vehicle> getAllVehiclesCreatedSince(final Date date){
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                "created",
                date
        );
        return searchInternal(request);
    }


    //search on basis of time range and payload
    public List<Vehicle> searchCreatedSince(final SearchRequestDTO dto, final Date date){
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                dto,
                date
        );
        return searchInternal(request);
    }

    //search requests in elastic search
    private List<Vehicle> searchInternal(SearchRequest request) {
        if(request ==null){
            LOG.error("Failed");
            return Collections.emptyList();
        }

        try{
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT); //searches in client  based on request
            final SearchHit[] searchHits = response.getHits().getHits();       //successful hits corresponding to search
            final List<Vehicle> vehicles = new ArrayList<>(searchHits.length);
            for(SearchHit hit: searchHits){
                vehicles.add(
                        MAPPER.readValue(hit.getSourceAsString(), Vehicle.class)  //convert hits to vehicle class
                );
            }
            return vehicles;   //return the result
        }catch(Exception e){
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Boolean index(final Vehicle vehicle){
        try{
            final String vehicleAsString = MAPPER.writeValueAsString(vehicle);
            final IndexRequest request = new IndexRequest(Indices.VEHICLE_INDEX);
            request.id(vehicle.getId());
            request.source(vehicleAsString, XContentType.JSON);

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            return response !=null && response.status().equals(RestStatus.OK);
        }catch(final Exception e){
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public Vehicle getById(final String vehicleId){
        try{
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.VEHICLE_INDEX, vehicleId),
                    RequestOptions.DEFAULT
            );
            if(documentFields == null || documentFields.isSourceEmpty()){
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), Vehicle.class);
        }catch(final Exception e){
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

}
