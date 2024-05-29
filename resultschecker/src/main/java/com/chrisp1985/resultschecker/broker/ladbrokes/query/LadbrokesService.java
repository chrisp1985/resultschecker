package com.chrisp1985.resultschecker.broker.ladbrokes.query;

import com.chrisp1985.resultschecker.broker.ladbrokes.client.LadbrokesClient;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "application.broker", havingValue = "Ladbrokes")
public class LadbrokesService {

    private final LadbrokesClient brokerClient;

    @Autowired
    public LadbrokesService(LadbrokesClient brokerClient) {

        this.brokerClient = brokerClient;

    }

    public ResponseModel getResponseFromClient(Long eventId) {
        return brokerClient.queryEventEndpoint(brokerClient.getPathForEventId(eventId));
    }


}
