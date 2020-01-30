package com.san.ticket.search.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.san.ticket.search.model.elastic.Ticket;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TicketResponse extends Ticket {

    private static final long serialVersionUID = -5718601516552545520L;

    @JsonProperty(value = "submitter_name")
    private String submitterName;

    @JsonProperty(value = "assignee_name")
    private String assigneeName;

    @JsonProperty(value = "organization_name")
    private String organizationName;

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
