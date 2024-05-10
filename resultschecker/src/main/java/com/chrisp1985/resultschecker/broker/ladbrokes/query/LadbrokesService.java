package com.chrisp1985.resultschecker.broker.ladbrokes.query;

import com.chrisp1985.resultschecker.broker.ladbrokes.client.LadbrokesClient;
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

    private final LadbrokesResponseParser ladbrokesResponseParser;

    @Autowired
    public LadbrokesService(LadbrokesClient brokerClient, LadbrokesResponseParser ladbrokesResponseParser) {

        this.brokerClient = brokerClient;
        this.ladbrokesResponseParser = ladbrokesResponseParser;

    }


}
