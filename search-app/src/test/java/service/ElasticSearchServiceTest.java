package service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.san.ticket.search.model.elastic.Organization;
import com.san.ticket.search.model.elastic.Ticket;
import com.san.ticket.search.model.elastic.User;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class ElasticSearchServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceTest.class);

    private ObjectMapper objectMapper = new SearchAppObjectMapper();

    @Test
    public void testDataLoading() {
        LOGGER.info("START: Init data.");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<User> users = getUsers();
        List<Ticket> tickets = getTickets();
        List<Organization> organizations = getOrganizations();
        LOGGER.info("END: Init data. Elapsed time: {} ms.", stopWatch.getLastTaskTimeMillis());
    }

    private List<User> getUsers() {
        return readValue(getInputStream("data/users.json"), new TypeReference<List<User>>() {});
    }

    private List<Ticket> getTickets() {
        return readValue(getInputStream("data/tickets.json"), new TypeReference<List<Ticket>>() {});
    }

    private List<Organization> getOrganizations() {
        return readValue(getInputStream("data/organizations.json"), new TypeReference<List<Organization>>() {});
    }

    private InputStream getInputStream(String path) {
        return ElasticSearchServiceTest.class.getClassLoader().getResourceAsStream(path);
    }

    private <T> T readValue(InputStream inputStream, TypeReference<T> typeReference) {
        try {
            LOGGER.info("Retrieving resource");
            String resourceStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return objectMapper.readValue(resourceStr, typeReference);
        } catch (IOException e) {
            LOGGER.error("Error in reading resource.", e);
            throw new RuntimeException("Init failed.");
        }
    }

    private static class SearchAppObjectMapper extends ObjectMapper {

        public SearchAppObjectMapper() {
            super();
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, Boolean.FALSE)
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, Boolean.TRUE)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, Boolean.TRUE)
                .configure(SerializationFeature.WRAP_ROOT_VALUE, Boolean.FALSE)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, Boolean.TRUE);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss X");
            this.setDateFormat(dateFormat);
            // do not serialize null value
            this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

    }

}
