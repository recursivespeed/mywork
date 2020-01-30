package com.san.ticket.search.model.elastic;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Ticket extends Base {

    private static final long serialVersionUID = -5718601516552545530L;

    private String type;

    private String subject;

    private String description;

    private String priority;

    private String status;

    @JsonProperty(value = "submitter_id")
    private String submitterId;

    @JsonProperty(value = "assignee_id")
    private String assigneeId;

    @JsonProperty(value = "organization_id")
    private String organizationId;

    @JsonProperty(value = "has_incidents")
    private Boolean hasIncidents;

    @JsonProperty(value = "due_at")
    private Date dueAt;

    private String via;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getHasIncidents() {
        return hasIncidents;
    }

    public void setHasIncidents(Boolean hasIncidents) {
        this.hasIncidents = hasIncidents;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public void setDueAt(Date dueAt) {
        this.dueAt = dueAt;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
