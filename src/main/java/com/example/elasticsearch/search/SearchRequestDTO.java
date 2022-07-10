package com.example.elasticsearch.search;

import java.util.List;

public class SearchRequestDTO {
    //fields payload
    private List<String> fields;
    //search term for applying query
    private String searchTerm;
    //sortBy which field
    private String sortBy;
    //ASCE or DESC
    private String order;
    //getters and setters
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
