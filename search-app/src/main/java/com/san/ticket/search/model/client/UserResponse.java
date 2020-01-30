package com.san.ticket.search.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.san.ticket.search.model.elastic.User;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserResponse extends User {

    private static final long serialVersionUID = -5718601516552545521L;

    @JsonProperty("organization_name")
    private String organizationName;

    private List<String> tickets;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
