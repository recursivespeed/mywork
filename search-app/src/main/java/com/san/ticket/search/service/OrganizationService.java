package com.san.ticket.search.service;


import static com.san.ticket.search.util.Constant.ORGANIZATION_INDEX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.san.ticket.search.error.SearchAppBadRequestException;
import com.san.ticket.search.model.elastic.Organization;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class OrganizationService extends AbstractService {

    @Autowired
    public OrganizationService(RestHighLevelClient restHighLevelClient,
        ObjectMapper objectMapper) {
        super(restHighLevelClient, objectMapper);
    }

    public void createDocument(Organization document) {
        IndexRequest indexRequest = new IndexRequest(ORGANIZATION_INDEX).id(document.getId())
            .source(convertDocumentToMap(document), XContentType.JSON);
        IndexResponse indexResponse = index(indexRequest);
        verify(indexResponse.getResult());
    }

    public void createDocuments(List<Organization> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            throw new SearchAppBadRequestException("Required organizations");
        }
        documents.stream().forEach(document -> createDocument(document));
    }

    public void updateDocument(Organization document) {
        Optional<Organization> optional = searchDocument("id", document.getId()).stream()
            .findFirst();
        Organization organization = optional.orElseThrow(SearchAppBadRequestException::new);
        UpdateRequest updateRequest = new UpdateRequest(ORGANIZATION_INDEX,
            String.valueOf(organization.getId()));
        updateRequest.doc(convertDocumentToMap(document));
        UpdateResponse updateResponse = update(updateRequest, RequestOptions.DEFAULT);
        verify(updateResponse.getResult());
    }

    public List<Organization> searchDocument(String fieldValue, String val) {
        SearchRequest searchRequest = buildSearchRequestByFieldAndVal(ORGANIZATION_INDEX, fieldValue, val);
        SearchResponse searchResponse = search(searchRequest);
        return getSearchResult(searchResponse);
    }

    public void deleteDocument(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(ORGANIZATION_INDEX, id);
        DeleteResponse response = delete(deleteRequest);
        verify(response.getResult());
    }

    private List<Organization> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<Organization> organizations = new ArrayList<>();
        for (SearchHit hit : searchHit){
            organizations.add(getObjectMapper().convertValue(hit.getSourceAsMap(), Organization.class));
        }
        return organizations;
    }

}
