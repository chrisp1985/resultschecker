package com.chrisp1985.resultschecker.service.update;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.chrisp1985.resultschecker.broker.ladbrokes.query.LadbrokesResponseParser;
import com.chrisp1985.resultschecker.broker.ladbrokes.query.LadbrokesService;
import com.chrisp1985.resultschecker.dao.DynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class DynamoUpdateService {

    DynamoDbDao dynamoDbDao;
    LadbrokesResponseParser ladbrokesResponseParser;
    DynamoRequestCreator dynamoRequestCreator;
    LadbrokesService ladbrokesService;

    @Autowired
    public DynamoUpdateService(DynamoDbDao dynamoDbDao,
                               LadbrokesResponseParser ladbrokesResponseParser,
                               DynamoRequestCreator dynamoRequestCreator,
                               LadbrokesService ladbrokesService) {

        this.dynamoDbDao = dynamoDbDao;
        this.ladbrokesResponseParser = ladbrokesResponseParser;
        this.dynamoRequestCreator = dynamoRequestCreator;
        this.ladbrokesService = ladbrokesService;

    }

    public void updateDynamoForEachEventId(List<Long> eventIds) {

        for(Long eventId : eventIds) {

            this.ladbrokesResponseParser.setResponse(this.ladbrokesService.getResponseFromClient(eventId));

            HashMap<String, AttributeValue> item_key = new HashMap<>() {{
                put("event_id", new AttributeValue().withS(String.valueOf(eventId)));
            }};

            int homeScore = this.ladbrokesResponseParser.getHomeScoreForEvent();
            int awayScore = this.ladbrokesResponseParser.getAwayScoreForEvent();

            if(!this.ladbrokesResponseParser.isResulted() || homeScore == 999) {

                dynamoDbDao.delete(item_key);

            }
            else {

                log.debug("Home: {}, Away: {}", homeScore, awayScore);

                sendUpdatedValuesToDynamo(item_key, dynamoRequestCreator.createUpdatedValuesMap(homeScore, awayScore, eventId));

            }
        }
    }

    public void sendUpdatedValuesToDynamo(HashMap<String, AttributeValue> item_key,
                                   HashMap<String, AttributeValueUpdate> values) {

        log.info("Updating Key {}", item_key);
        dynamoDbDao.update(item_key, values);

    }
}
