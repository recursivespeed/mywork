package com.san.ticket.search.service;


import static com.san.ticket.search.util.Constant.USER_INDEX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.san.ticket.search.error.SearchAppBadRequestException;
import com.san.ticket.search.model.elastic.User;
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
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UserService extends AbstractService {

    @Autowired
    public UserService(RestHighLevelClient restHighLevelClient,
        ObjectMapper objectMapper) {
        super(restHighLevelClient, objectMapper);
    }

    public void createDocument(User document) {
        IndexRequest indexRequest = new IndexRequest(USER_INDEX).id(document.getId())
            .source(convertDocumentToMap(document), XContentType.JSON);
        IndexResponse indexResponse = index(indexRequest);
        verify(indexResponse.getResult());
    }

    public void createDocuments(List<User> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            throw new SearchAppBadRequestException("Required users");
        }
        documents.stream().forEach(document -> createDocument(document));
    }

    public void updateDocument(User document) {
        Optional<User> userOptional = searchDocument("id", document.getId()).stream()
            .findFirst();
        User user = userOptional.orElseThrow(SearchAppBadRequestException::new);
        UpdateRequest updateRequest = new UpdateRequest(USER_INDEX, String.valueOf(user.getId()));
        updateRequest.doc(convertDocumentToMap(document));
        UpdateResponse updateResponse = update(updateRequest);
        verify(updateResponse.getResult());
    }

    public List<User> searchDocument(String fieldValue, String val) {
        SearchRequest searchRequest = buildSearchRequestByFieldAndVal(USER_INDEX, fieldValue, val);
        SearchResponse searchResponse = search(searchRequest);
        return getSearchResult(searchResponse);
    }

    public void deleteDocument(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(USER_INDEX, id);
        DeleteResponse response = delete(deleteRequest);
        verify(response.getResult());
    }

    private List<User> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<User> users = new ArrayList<>();
        for (SearchHit hit : searchHit){
            users.add(getObjectMapper().convertValue(hit.getSourceAsMap(), User.class));
        }
        return users;
    }

}
