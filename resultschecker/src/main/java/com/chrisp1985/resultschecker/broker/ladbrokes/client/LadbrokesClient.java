package com.chrisp1985.resultschecker.broker.ladbrokes.client;

import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class LadbrokesClient {

    private final ObjectMapper objectMapper;
    private final RestClient ladbrokesRestClient;

    @Autowired
    public LadbrokesClient(ObjectMapper objectMapper, RestClient ladbrokesRestClient) {
        this.objectMapper = objectMapper;
        this.ladbrokesRestClient = ladbrokesRestClient;
    }

    public ResponseModel queryEventEndpoint(String path) {

        var responseLadbrokes = ladbrokesRestClient.get()
                .uri(path)
                .retrieve()
                .toEntity(ResponseModel.class)
                .getBody();

        try {
            log.info("Response Ladbrokes : {}", objectMapper.writeValueAsString(responseLadbrokes));
        } catch (JsonProcessingException ignored) {
            log.debug("Response Ladbrokes : {}", responseLadbrokes);
        }
        return responseLadbrokes;
    }

    public String getPathForEventId(long eventId) {
        return String.format("/openbet-ssviewer/Commentary/2.31/CommentaryForEvent/%d" +
                "?translationLang=en&responseFormat=json&includeUndisplayed=true", eventId);
    }
}
