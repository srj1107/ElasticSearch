package com.example.elasticsearch.search.util;


import com.example.elasticsearch.search.SearchRequestDTO;
import org.apache.lucene.util.CollectionUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

//class used to build search requests
public class SearchUtil {

    private SearchUtil(){}

    //build search request on basis of payload
    public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto){
        try{
            //allows to build searching in elastic search
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(dto));    //filter the data on basis of dto
            if(dto.getSortBy() !=null){                   //check if we need to sort data
                builder = builder.sort(
                        dto.getSortBy(),                  //field on basis of which we want to sort
                        dto.getOrder() != null ? SortOrder.valueOf(dto.getOrder()) : SortOrder.ASC //sorting type
                );
            }

            //return search request data
            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;
        }catch (final Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //build search request on basis of date
    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final String field,
                                                   final Date date){
        try{
            final SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(field, date));  //filter data on basis of date
            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;                                      //return search request data
        }catch(final Exception e){
            e.printStackTrace();                                 //print stack trace of exceptions logged
            return null;
        }
    }

    //search on basis of payload and date range
    public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto, final Date date){
        try{
            final QueryBuilder searchQuery = getQueryBuilder(dto);   //create a queryBuilder on basis of payload
            final QueryBuilder dateQuery = getQueryBuilder("created", date);  //create a query builder on basis of time range
            //merge the both queries and use it for filtering data
            final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(searchQuery).must(dateQuery);

            //passing the merged queries for searching
            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(boolQuery);
            if(dto.getSortBy() !=null){             //check if we need to sort data
                builder = builder.sort(
                        dto.getSortBy(),            //field on basis of which we need to sort
                        dto.getOrder() != null ? SortOrder.valueOf(dto.getOrder()) : SortOrder.ASC    //sorting type
                );
            }
            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;                                    //return the resolved data
        }catch (final Exception e){
            e.printStackTrace();                               //print stack trace in logs
            return null;
        }
    }

    //generate query builder for dto
    private static QueryBuilder getQueryBuilder(final SearchRequestDTO dto){
        if(dto==null){
            return null;                          //no builder generated
        }

        final List<String> fields = dto.getFields();             //field on which we want to get data
        if(CollectionUtils.isEmpty(fields)){
            return null;                                         //if field list is empty return null builder
        }

        if(fields.size()>1){                                     //filter on basis of more than one filed
            final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())   //match the search term in every document
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)                    //return data on basis of cross fields
                    .operator(Operator.OR);                                            //apply method

            fields.forEach(queryBuilder::field);                                       //return data on based of specified fields

            return queryBuilder;                                                       //return query
        }

        //if fields size ==1
        return fields.stream().findFirst()
                .map(field -> QueryBuilders.matchQuery(field, dto.getSearchTerm()).operator(Operator.AND))
                .orElse(null);
    }

    //generate query builder on basis of time range query
    private static QueryBuilder getQueryBuilder(final String field, final Date date){   //here field = "created" apply time range query on created field
        return QueryBuilders.rangeQuery(field).gte(date);
    }
}
