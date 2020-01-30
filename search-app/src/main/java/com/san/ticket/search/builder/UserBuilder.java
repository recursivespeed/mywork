package com.san.ticket.search.builder;

import com.san.ticket.search.model.client.UserRequest;
import com.san.ticket.search.model.client.UserResponse;
import com.san.ticket.search.model.elastic.Organization;
import com.san.ticket.search.model.elastic.Ticket;
import com.san.ticket.search.model.elastic.User;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class UserBuilder {

    private ModelMapper mapper = new ModelMapper();

    private User user;

    private Organization organization;

    private List<Ticket> tickets;

    private UserRequest request;

    public UserBuilder user(User user) {
        this.user = user;
        return this;
    }

    public UserBuilder organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public UserBuilder tickets(List<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public UserBuilder request(UserRequest request) {
        this.request = request;
        return this;
    }

    public UserResponse buildResponse() {
        if (user != null) {
            UserResponse response = mapper.map(user, UserResponse.class);
            if (response != null) {
                if (organization != null) {
                    response.setOrganizationName(organization.getName());
                }
                response.setTickets(tickets.stream().map(Ticket::getSubject)
                    .collect(Collectors.toList()));
            }
            return response;
        } else {
            throw new IllegalArgumentException("Not enough required parameters");
        }
    }

    public User buildDocument() {
        if (request != null) {
            return mapper.map(request, User.class);
        } else {
            throw new IllegalArgumentException("Not enough required parameters");
        }
    }

}
