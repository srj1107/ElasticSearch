package com.example.elasticsearch.repository;

import com.example.elasticsearch.documents.Vehicle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends ElasticsearchRepository<Vehicle, String> {

}
