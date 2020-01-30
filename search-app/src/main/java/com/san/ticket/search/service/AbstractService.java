package com.san.ticket.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.san.ticket.search.error.SearchAppUnexpectedException;
import java.io.IOException;
import java.util.Map;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    @Autowired
    public AbstractService(RestHighLevelClient restHighLevelClient,
        ObjectMapper objectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    protected SearchRequest buildSearchRequest(String index) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        return searchRequest;
    }

    protected SearchRequest buildSearchRequestByFieldAndVal(String index, String fieldValue, String val){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldValue, val)
            .operator(Operator.AND);
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    protected RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }

    protected IndexResponse index(IndexRequest request) {
        return index(request, RequestOptions.DEFAULT);
    }

    protected IndexResponse index(IndexRequest request, RequestOptions options) {
        try {
            return restHighLevelClient.index(request, options);
        } catch (IOException e) {
            logger.error("Error when pushing request.", e);
            throw new SearchAppUnexpectedException(e);
        }
    }

    protected UpdateResponse update(UpdateRequest request) {
        return update(request, RequestOptions.DEFAULT);
    }

    protected UpdateResponse update(UpdateRequest request, RequestOptions options) {
        try {
            return restHighLevelClient.update(request, options);
        } catch (IOException e) {
            logger.error("Error when pushing request.", e);
            throw new SearchAppUnexpectedException(e);
        }
    }

    protected SearchResponse search(SearchRequest request) {
        return search(request, RequestOptions.DEFAULT);
    }

    protected SearchResponse search(SearchRequest request, RequestOptions options) {
        try {
            return restHighLevelClient.search(request, options);
        } catch (IOException e) {
            logger.error("Error when pushing request.", e);
            throw new SearchAppUnexpectedException(e);
        }
    }

    protected DeleteResponse delete(DeleteRequest request) {
        return delete(request, RequestOptions.DEFAULT);
    }

    protected DeleteResponse delete(DeleteRequest request, RequestOptions options) {
        try {
            return restHighLevelClient.delete(request, options);
        } catch (IOException e) {
            logger.error("Error when pushing request.", e);
            throw new SearchAppUnexpectedException(e);
        }
    }

    protected <T> Map<String, Object> convertDocumentToMap(T t) {
        return getObjectMapper().convertValue(t, Map.class);
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected Logger getLogger() {
        return logger;
    }

    protected void verify(Result result) {
        if (result != Result.CREATED && result != Result.UPDATED) {
            throw new SearchAppUnexpectedException("Invalid status: " + result.name());
        }
    }

}
