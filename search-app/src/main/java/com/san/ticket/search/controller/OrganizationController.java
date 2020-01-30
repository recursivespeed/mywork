package com.san.ticket.search.controller;


import com.san.ticket.search.model.client.OrganizationResponse;
import com.san.ticket.search.model.elastic.Organization;
import com.san.ticket.search.service.ElasticSearchService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    private ElasticSearchService service;

    @Autowired
    public OrganizationController(ElasticSearchService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(path = "/organizations", method = { RequestMethod.POST })
    public ResponseEntity createOrganization(@RequestBody Organization document) {
        service.createDocument(document);
        return new ResponseEntity("OK", HttpStatus.CREATED);
    }

    @RequestMapping(path = "/organizations/generic-search", method = { RequestMethod.GET })
    public List<OrganizationResponse> genericSearch(@RequestParam(value = "field") String field,
        @RequestParam(value = "val") String val ) {
        return service.searchOrganizationDocument(field, val);
    }

}