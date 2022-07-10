package com.example.elasticsearch.service;

import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Util;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class IndexService {

    //log events for index service class
    private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);
    //get vehicle as string
    private static final List<String> INDICES = List.of(Indices.VEHICLE_INDEX);

    //use elasticsearch client to create indices
    private final RestHighLevelClient client;

    //constructor
    @Autowired // Inject bean dependency
    public IndexService(RestHighLevelClient client){
        this.client= client;
    }


    @PostConstruct //when the constructor is called the bean is not initialized, when the bean is initialized the method is called
    public void tryToCreateIndices() {
        recreateIndices(false);
    }

    //use to recreate indices
    public void recreateIndices(final boolean deleteExisting) {

        //bring string "index" here
        final String settings = Util.loadAsString("static/es-settings.json");

        if(settings==null){
            LOG.error("Failed to load index settings");
            return;
        }

        //iterate on every indices and try to create one
        for(final String indexName: INDICES){
            try{
                //check if the index already exists
                final boolean indexExists = client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
                //if exists check do we want to delete the existing
                if(indexExists){
                    if(!deleteExisting){
                        continue;
                    }
                    client.indices().delete(
                            new DeleteIndexRequest(indexName),
                            RequestOptions.DEFAULT
                    );
                }
                //create index
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(settings, XContentType.JSON);

                //
                final String mappings = loadMappings(indexName);
                if(mappings!=null){
                    //if index is created generate mappings(schema-like)
                    createIndexRequest.mapping(mappings, XContentType.JSON);
                }
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            }catch(final Exception e){
                LOG.error(e.getMessage(), e);
            }
        }
    }

    //load mappings (inject a map(key, value) from another file) with the help of index
    private String loadMappings(String indexName){
        final String mappings = Util.loadAsString("static/mappings/" +indexName+".json");
        if(mappings==null){
            LOG.error("Failed to load mappings for index wih name '{}'", indexName);
            return null;
        }
        return mappings;
    }
}
