package com.chrisp1985.resultschecker.service;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import com.chrisp1985.resultschecker.broker.ladbrokes.query.LadbrokesResponseParser;
import com.chrisp1985.resultschecker.dao.DynamoDbDao;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class UpdateService {

    DynamoDbDao dynamoDbDao;
    ReadService readService;
    LadbrokesResponseParser ladbrokesResponseParser;

    @Autowired
    public UpdateService(DynamoDbDao dynamoDbDao, ReadService readService, LadbrokesResponseParser ladbrokesResponseParser) {
        this.readService = readService;
        this.dynamoDbDao = dynamoDbDao;
        this.ladbrokesResponseParser = ladbrokesResponseParser;
    }

    @PostConstruct
    void process() {
        List<Long> eventIds = getAllEventsWithoutResults();

        for(Long eventId : eventIds) {

            ResponseModel responseModel = readService.getResponseFromClient(eventId);
            HashMap<String, AttributeValue> item_key = new HashMap<>();
            item_key.put("event_id", new AttributeValue().withS(String.valueOf(eventId)));

            if(this.ladbrokesResponseParser.isResulted(responseModel)) {
                int homeScore = this.ladbrokesResponseParser.getHomeScoreForEvent(responseModel);
                int awayScore = this.ladbrokesResponseParser.getAwayScoreForEvent(responseModel);
                System.out.printf("Home: %d, Away: %d\n", homeScore, awayScore);

                HashMap<String, AttributeValueUpdate> updated_values =
                        new HashMap<String, AttributeValueUpdate>();
                updated_values.put("home_goals_result",
                        new AttributeValueUpdate(new AttributeValue().withN(String.valueOf(homeScore)), AttributeAction.PUT));
                updated_values.put("away_goals_result",
                        new AttributeValueUpdate(new AttributeValue().withN(String.valueOf(awayScore)), AttributeAction.PUT));
                updated_values.put("results_timestamp",
                        new AttributeValueUpdate(new AttributeValue(getTimestampToAddToDb()), AttributeAction.PUT));
                updated_values.put("away_win",
                        new AttributeValueUpdate(new AttributeValue().withBOOL(awayScore > homeScore), AttributeAction.PUT));

                dynamoDbDao.update(item_key, updated_values);

            }
            else {
                dynamoDbDao.delete(item_key);
            }
        }
    }

    public String getTimestampToAddToDb() {

        String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(datePattern);
        return europeanDateFormatter.format(LocalDateTime.now().atOffset(ZoneOffset.UTC)) + ".000Z";

    }

    List<Long> getAllEventsWithoutResults() {
        return readService.getEventIdListFromDynamo();
    }
}
