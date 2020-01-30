package com.san.ticket.search.service;

import static com.san.ticket.search.util.Constant.ORGANIZATION_RESPONSE_INDEX;
import static com.san.ticket.search.util.Constant.TICKET_RESPONSE_INDEX;
import static com.san.ticket.search.util.Constant.USER_RESPONSE_INDEX;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.san.ticket.search.builder.OrganizationBuilder;
import com.san.ticket.search.builder.TicketBuilder;
import com.san.ticket.search.builder.UserBuilder;
import com.san.ticket.search.error.SearchAppBadRequestException;
import com.san.ticket.search.model.client.OrganizationRequest;
import com.san.ticket.search.model.client.OrganizationResponse;
import com.san.ticket.search.model.client.TicketRequest;
import com.san.ticket.search.model.client.TicketResponse;
import com.san.ticket.search.model.client.UserRequest;
import com.san.ticket.search.model.client.UserResponse;
import com.san.ticket.search.model.elastic.Organization;
import com.san.ticket.search.model.elastic.Ticket;
import com.san.ticket.search.model.elastic.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

@Service
public class ElasticSearchService extends AbstractService {

    @Value("classpath:data/users.json")
    private Resource usersResource;

    @Value("classpath:data/tickets.json")
    private Resource ticketsResource;

    @Value("classpath:data/organizations.json")
    private Resource organizationsResource;

    private UserService userService;

    private TicketService ticketService;

    private OrganizationService organizationService;

    @Autowired
    public ElasticSearchService(RestHighLevelClient restHighLevelClient,
        ObjectMapper objectMapper, UserService userService,
        TicketService ticketService,
        OrganizationService organizationService) {
        super(restHighLevelClient, objectMapper);
        this.userService = userService;
        this.ticketService = ticketService;
        this.organizationService = organizationService;
    }

    public void createDocument(UserRequest userRequest) {
        if (userRequest == null) {
            throw new SearchAppBadRequestException();
        }
        User user = new UserBuilder().request(userRequest).buildDocument();
        userService.createDocument(user);
        createDocument(user);
    }

    public void createDocument(User document) {
        getLogger().info("START:Create joint document for user {}", document.getId());
        UserResponse userResponse = buildUserResponse(document);
        IndexRequest indexRequest = new IndexRequest(USER_RESPONSE_INDEX).id(document.getId())
            .source(convertDocumentToMap(userResponse), XContentType.JSON);
        IndexResponse indexResponse = index(indexRequest);
        verify(indexResponse.getResult());
        getLogger().info("END:Create joint document for user {}", document.getId());
    }

    public void createDocument(OrganizationRequest organizationRequest) {
        if (organizationRequest == null) {
            throw new SearchAppBadRequestException();
        }
        Organization organization = new OrganizationBuilder().request(organizationRequest)
            .buildOrganization();
        organizationService.createDocument(organization);
        createDocument(organization);
    }

    public void createDocument(Organization document) {
        getLogger().info("START:Create joint document for organization {}", document.getId());
        OrganizationResponse organizationResponse = new OrganizationBuilder().organization(document)
            .buildOrganizationResponse();
        IndexRequest indexRequest = new IndexRequest(ORGANIZATION_RESPONSE_INDEX).id(document.getId())
            .source(convertDocumentToMap(organizationResponse), XContentType.JSON);
        IndexResponse indexResponse = index(indexRequest);
        verify(indexResponse.getResult());
        getLogger().info("END:Create joint document for organization {}", document.getId());
    }

    public void createDocument(TicketRequest request) {
        if (request == null) {
            throw new SearchAppBadRequestException();
        }
        Ticket document = new TicketBuilder().request(request).buildDocument();
        ticketService.createDocument(document);
        createDocument(document);
    }

    public void createDocument(Ticket document) {
        getLogger().info("START:Create joint document for ticket {}", document.getId());
        TicketResponse ticketResponse = buildTicketResponse(document);
        IndexRequest indexRequest = new IndexRequest(TICKET_RESPONSE_INDEX).id(document.getId())
            .source(convertDocumentToMap(ticketResponse), XContentType.JSON);
        IndexResponse indexResponse = index(indexRequest);
        verify(indexResponse.getResult());
        getLogger().info("END:Create joint document for ticket {}", document.getId());
    }

    public void createDocuments(List<UserRequest> userRequests) {
        if (CollectionUtils.isEmpty(userRequests)) {
            throw new SearchAppBadRequestException();
        }
        userRequests.stream().forEach(this::createDocument);
    }

    public List<UserResponse> searchUserDocument(String fieldValue, String val) {
        SearchRequest searchRequest = buildSearchRequestByFieldAndVal(USER_RESPONSE_INDEX, fieldValue, val);
        SearchResponse searchResponse = search(searchRequest);
        return getSearchResult(searchResponse, UserResponse.class);
    }

    public List<TicketResponse> searchTicketDocument(String fieldValue, String val) {
        SearchRequest searchRequest = buildSearchRequestByFieldAndVal(TICKET_RESPONSE_INDEX, fieldValue, val);
        SearchResponse searchResponse = search(searchRequest);
        return getSearchResult(searchResponse, TicketResponse.class);
    }

    public List<OrganizationResponse> searchOrganizationDocument(String fieldValue, String val) {
        SearchRequest searchRequest = buildSearchRequestByFieldAndVal(ORGANIZATION_RESPONSE_INDEX,
            fieldValue, val);
        SearchResponse searchResponse = search(searchRequest);
        return getSearchResult(searchResponse, OrganizationResponse.class);
    }

    @PostConstruct
    public void setup() {
        getLogger().info("START: Init data.");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        init();
        stopWatch.stop();
        getLogger().info("END: Init data. Elapsed time: {} ms.", stopWatch.getLastTaskTimeMillis());
    }

    private void init() {
        List<User> users = getUsers();
        List<Ticket> tickets = getTickets();
        List<Organization> organizations = getOrganizations();
        getLogger().info("Insert users.");
        userService.createDocuments(users);
        getLogger().info("Insert tickets.");
        ticketService.createDocuments(tickets);
        getLogger().info("Insert organizations.");
        organizationService.createDocuments(organizations);

        organizations.stream().forEach(this::createDocument);
        users.stream().forEach(this::createDocument);
        tickets.stream().forEach(this::createDocument);
    }

    private List<User> getUsers() {
        return readValue(usersResource, new TypeReference<List<User>>() {});
    }

    private List<Ticket> getTickets() {
        return readValue(ticketsResource, new TypeReference<List<Ticket>>() {});
    }

    private List<Organization> getOrganizations() {
        return readValue(organizationsResource, new TypeReference<List<Organization>>() {});
    }

    private <T> T readValue(Resource resource, TypeReference<T> typeReference) {
        try {
            getLogger().info("Retrieving resource, resource=" + resource.getFilename());
            String resourceStr = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            return getObjectMapper().readValue(resourceStr, typeReference);
        } catch (IOException e) {
            getLogger().error("Error in reading resource.", e);
            throw new RuntimeException("Init failed.");
        }
    }

    private List<Ticket> findAllAssociatedTickets(String userId) {
        List<Ticket> tickets = new ArrayList<>();
        List<Ticket> submitterIds = ticketService.searchDocument("submitter_id", userId);
        if (!CollectionUtils.isEmpty(submitterIds)) {
            tickets.addAll(submitterIds);
        }
        List<Ticket> assigneeId = ticketService.searchDocument("assignee_id", userId);
        if (!CollectionUtils.isEmpty(assigneeId)) {
            tickets.addAll(assigneeId);
        }
        return tickets;
    }

    private <T> List<T> getSearchResult(SearchResponse response, Class<T> clazz) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<T> documents = new ArrayList<>();
        for (SearchHit hit : searchHit){
            documents.add(getObjectMapper().convertValue(hit.getSourceAsMap(), clazz));
        }
        return documents;
    }

    private UserResponse buildUserResponse(User document) {
        String userId = document.getId();
        String organizationId = document.getOrganizationId();
        UserBuilder builder = new UserBuilder();
        if (organizationId != null) {
            Optional<Organization> orgOptional = organizationService.searchDocument("id",
                organizationId).stream().findFirst();
            Organization organization = orgOptional.orElse(null);
            builder.organization(organization);
        }
        List<Ticket> tickets = findAllAssociatedTickets(userId);
        UserResponse userResponse = builder.user(document).tickets(tickets).buildResponse();
        return userResponse;
    }

    private TicketResponse buildTicketResponse(Ticket document) {
        String submitterId = document.getSubmitterId();
        String assigneeId = document.getAssigneeId();
        String organizationId = document.getOrganizationId();
        TicketBuilder builder = new TicketBuilder();
        builder.ticket(document);
        if (submitterId != null) {
            Optional<User> subOptional = userService.searchDocument("id",
                submitterId).stream().findFirst();
            User submitterUser = subOptional.orElse(null);
            builder.submitterUser(submitterUser);
        }
        if (assigneeId != null) {
            Optional<User> assigneeOptional = userService.searchDocument("id",
                assigneeId).stream().findFirst();
            User assigneeUser = assigneeOptional.orElse(null);
            builder.assigneeUser(assigneeUser);
        }
        if (organizationId != null) {
            Optional<Organization> orgOptional = organizationService.searchDocument("id",
                organizationId).stream().findFirst();
            Organization organization = orgOptional.orElse(null);
            builder.organization(organization);
        }
        TicketResponse response = builder.buildResponse();
        return response;
    }

}
