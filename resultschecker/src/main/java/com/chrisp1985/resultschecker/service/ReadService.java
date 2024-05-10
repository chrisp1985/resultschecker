package com.chrisp1985.resultschecker.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.*;
import com.chrisp1985.resultschecker.broker.ladbrokes.client.LadbrokesClient;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import com.chrisp1985.resultschecker.dao.DynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReadService {

    private DynamoDbDao dynamoDbDao;

    private LadbrokesClient ladbrokesClient;

    @Autowired
    public ReadService(DynamoDbDao dynamoDbDao, LadbrokesClient ladbrokesClient) {
        this.ladbrokesClient = ladbrokesClient;
        this.dynamoDbDao = dynamoDbDao;

    }

    private HashMap<String, Condition> resultsWithNoTimestampFilter() {

        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition resultsTimestampEmpty = new Condition()
                .withComparisonOperator(
                        ComparisonOperator.EQ.toString())
                .withAttributeValueList(
                        new AttributeValue("EMPTY"));

        Condition startTimeGreaterThanOneDay = new Condition()
                .withComparisonOperator(
                        ComparisonOperator.NOT_CONTAINS.toString())
                .withAttributeValueList(
                        new AttributeValue(getDateToday()));

        scanFilter.put("results_timestamp", resultsTimestampEmpty);
        scanFilter.put("start_time", startTimeGreaterThanOneDay);
        return scanFilter;

    }

    public static String getDateToday() {
        String datePattern = "yyyy-MM-dd'T'";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(datePattern);
        return europeanDateFormatter.format(LocalDateTime.now().atOffset(ZoneOffset.UTC));
    }

    public List<Long> getEventIdListFromDynamo() {

        try {

            return dynamoDbDao.read(resultsWithNoTimestampFilter()).getItems()
                    .stream()
                    .map(s -> Long.valueOf(s.get("event_id").getS()))
                    .collect(Collectors.toList());

        } catch (AmazonServiceException e) {

            log.error("Found an error: {}. Be sure that it exists and that you've typed its name correctly!", e.getErrorMessage());
            System.exit(1);
            return new ArrayList<>();
        }

    }

    public ResponseModel getResponseFromClient(Long eventId) {
        return ladbrokesClient.queryEventEndpoint(ladbrokesClient.getPathForEventId(eventId));
    }
}
