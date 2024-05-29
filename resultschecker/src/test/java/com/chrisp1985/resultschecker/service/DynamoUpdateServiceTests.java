package com.chrisp1985.resultschecker.service;
import com.chrisp1985.resultschecker.broker.ladbrokes.query.LadbrokesResponseParser;
import com.chrisp1985.resultschecker.broker.ladbrokes.query.LadbrokesService;
import com.chrisp1985.resultschecker.dao.DynamoDbDao;
import com.chrisp1985.resultschecker.service.update.DynamoRequestCreator;
import com.chrisp1985.resultschecker.service.update.DynamoUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DynamoUpdateServiceTests {

    @Mock
    DynamoDbDao dynamoDbDao;

    @Mock
    LadbrokesResponseParser ladbrokesResponseParser;

    @Mock
    LadbrokesService ladbrokesService;

    @Mock
    DynamoRequestCreator dynamoRequestCreator;

    DynamoUpdateService dynamoUpdateService;


    @BeforeEach
    void init() {

        this.dynamoUpdateService = new DynamoUpdateService(dynamoDbDao, ladbrokesResponseParser, dynamoRequestCreator, ladbrokesService);

    }

    @Test
    void UpdateService_createUpdatedValuesMap_ttt() {



    }

    @Test
    void UpdateService_updateDynamoForEachEventId_validEventIdsList() {

        List<Long> eventIds = List.of(12345L, 67890L);

        dynamoUpdateService.updateDynamoForEachEventId(eventIds);

    }
}
