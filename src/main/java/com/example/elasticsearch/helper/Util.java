package com.example.elasticsearch.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.nio.file.Files;

public class Util {

    //used to trigger log events for util class
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    //convert file path to string
    public static String loadAsString(final String path){
        try{
            final File resource = new ClassPathResource(path).getFile(); //access a file by it's path
            return new String(Files.readAllBytes(resource.toPath()));  //return path of file in string format
        }catch(final Exception e){
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
