package com.san.ticket.search.model.elastic;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Organization extends Base {

    private static final long serialVersionUID = -5718601516552545534L;

    private String name;

    @JsonProperty(value = "domain_names")
    private List<String> domainNames;

    private String details;

    @JsonProperty(value = "shared_tickets")
    private Boolean sharedTickets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDomainNames() {
        return domainNames;
    }

    public void setDomainNames(List<String> domainNames) {
        this.domainNames = domainNames;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getSharedTickets() {
        return sharedTickets;
    }

    public void setSharedTickets(Boolean sharedTickets) {
        this.sharedTickets = sharedTickets;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
