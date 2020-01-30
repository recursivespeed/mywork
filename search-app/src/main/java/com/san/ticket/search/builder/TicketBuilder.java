package com.san.ticket.search.builder;

import com.san.ticket.search.model.client.TicketRequest;
import com.san.ticket.search.model.client.TicketResponse;
import com.san.ticket.search.model.elastic.Organization;
import com.san.ticket.search.model.elastic.Ticket;
import com.san.ticket.search.model.elastic.User;
import org.modelmapper.ModelMapper;

public class TicketBuilder {

    private ModelMapper mapper = new ModelMapper();

    private Ticket ticket;

    private User submitterUser;

    private User assigneeUser;

    private Organization organization;

    private TicketRequest request;

    public TicketBuilder ticket(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public TicketBuilder submitterUser(User submitterUser) {
        this.submitterUser = submitterUser;
        return this;
    }

    public TicketBuilder assigneeUser(User assigneeUser) {
        this.assigneeUser = assigneeUser;
        return this;
    }

    public TicketBuilder organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public TicketBuilder request(TicketRequest request) {
        this.request = request;
        return this;
    }

    public TicketResponse buildResponse() {
        if (ticket != null) {
            TicketResponse response = mapper.map(ticket, TicketResponse.class);
            if (response != null) {
                if (organization != null) {
                    response.setOrganizationName(organization.getName());
                }
                if (submitterUser != null) {
                    response.setSubmitterName(submitterUser.getName());
                }
                if (assigneeUser != null) {
                    response.setAssigneeName(assigneeUser.getName());
                }
            }
            return response;
        } else {
            throw new IllegalArgumentException("Not enough required parameters");
        }
    }

    public Ticket buildDocument() {
        if (request != null) {
            return mapper.map(request, Ticket.class);
        } else {
            throw new IllegalArgumentException("Not enough required parameters");
        }
    }

}
