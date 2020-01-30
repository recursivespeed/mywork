package com.san.ticket.search.builder;

import com.san.ticket.search.model.client.OrganizationRequest;
import com.san.ticket.search.model.client.OrganizationResponse;
import com.san.ticket.search.model.elastic.Organization;
import org.modelmapper.ModelMapper;

public class OrganizationBuilder {

    private ModelMapper mapper = new ModelMapper();

    private Organization organization;

    private OrganizationRequest request;

    public OrganizationBuilder organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public OrganizationBuilder request(OrganizationRequest request) {
        this.request = request;
        return this;
    }

    public Organization buildOrganization() {
        if (request != null) {
            return mapper.map(request, Organization.class);
        } else {
            throw new IllegalArgumentException("Not enough required parameters");
        }
    }

    public OrganizationResponse buildOrganizationResponse() {
        if (organization != null) {
            return mapper.map(organization, OrganizationResponse.class);
        } else {
            throw new IllegalArgumentException("Not enough required parameters");
        }
    }

}
