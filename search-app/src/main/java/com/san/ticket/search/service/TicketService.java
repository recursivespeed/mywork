package com.san.ticket.search.service;


import static com.san.ticket.search.util.Constant.TICKET_INDEX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.san.ticket.search.error.SearchAppBadRequestException;
import com.san.ticket.search.model.elastic.Ticket;
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
public class TicketService extends AbstractService {

    @Autowired
    public TicketService(RestHighLevelClient restHighLevelClient,
        ObjectMapper objectMapper) {
        super(restHighLevelClient, objectMapper);
    }

    public void createDocument(Ticket document) {
        IndexRequest indexRequest = new IndexRequest(TICKET_INDEX).id(document.getId())
            .source(convertDocumentToMap(document), XContentType.JSON);
        IndexResponse indexResponse = index(indexRequest);
        verify(indexResponse.getResult());
    }

    public void createDocuments(List<Ticket> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            throw new SearchAppBadRequestException("Required tickets");
        }
        documents.stream().forEach(document -> createDocument(document));
    }

    public void updateDocument(Ticket document) {
        Optional<Ticket> optional = searchDocument("id", document.getId()).stream()
            .findFirst();
        Ticket ticket = optional.orElseThrow(SearchAppBadRequestException::new);
        UpdateRequest updateRequest = new UpdateRequest(TICKET_INDEX, String.valueOf(ticket.getId()));
        updateRequest.doc(convertDocumentToMap(document));
        UpdateResponse updateResponse = update(updateRequest, RequestOptions.DEFAULT);
        verify(updateResponse.getResult());
    }

    public List<Ticket> searchDocument(String fieldValue, String val) {
        SearchRequest searchRequest = buildSearchRequestByFieldAndVal(TICKET_INDEX, fieldValue, val);
        SearchResponse searchResponse = search(searchRequest);
        return getSearchResult(searchResponse);
    }

    public void deleteDocument(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(TICKET_INDEX, id);
        DeleteResponse response = delete(deleteRequest);
        verify(response.getResult());
    }

    private List<Ticket> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<Ticket> tickets = new ArrayList<>();
        for (SearchHit hit : searchHit){
            tickets.add(getObjectMapper().convertValue(hit.getSourceAsMap(), Ticket.class));
        }
        return tickets;
    }

}
