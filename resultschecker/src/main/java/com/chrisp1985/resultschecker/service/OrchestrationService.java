package com.chrisp1985.resultschecker.service;

import com.chrisp1985.resultschecker.service.read.DynamoReadService;
import com.chrisp1985.resultschecker.service.update.DynamoUpdateService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class OrchestrationService {
    DynamoReadService readService;
    DynamoUpdateService dynamoUpdateService;

    @Autowired
    public OrchestrationService(DynamoReadService readService, DynamoUpdateService dynamoUpdateService) {
        this.readService = readService;
        this.dynamoUpdateService = dynamoUpdateService;
    }

    @PostConstruct
    void getEventIdsAndUpdate() {

        List<Long> eventIds = readService.getEventIdListFromDynamo();
        this.dynamoUpdateService.updateDynamoForEachEventId(eventIds);

    }


}
