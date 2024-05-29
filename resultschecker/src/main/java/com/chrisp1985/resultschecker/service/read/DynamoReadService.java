package com.chrisp1985.resultschecker.service.read;

import com.amazonaws.AmazonServiceException;
import com.chrisp1985.resultschecker.dao.DynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DynamoReadService {

    private final DynamoDbDao dynamoDbDao;

    private final DynamoReadFilter dynamoReadFilter;

    @Autowired
    public DynamoReadService(DynamoDbDao dynamoDbDao, DynamoReadFilter dynamoReadFilter) {
        this.dynamoReadFilter = dynamoReadFilter;
        this.dynamoDbDao = dynamoDbDao;

    }

    public List<Long> getEventIdListFromDynamo() {

        try {

            return dynamoDbDao.read(this.dynamoReadFilter.resultsWithNoTimestampFilter()).getItems()
                    .stream()
                    .map(s -> Long.valueOf(s.get("event_id").getS()))
                    .collect(Collectors.toList());

        } catch (AmazonServiceException e) {

            log.error("Found an error: {}. Be sure that it exists and that you've typed its name correctly!", e.getErrorMessage());
            System.exit(1);
            return new ArrayList<>();
        }

    }

    private Double getDynamoColumnValueForEvent(String columnName, Long eventId) {

        try {

            return dynamoDbDao.read(this.dynamoReadFilter.resultsWithNoTimestampFilter()).getItems()
                    .stream()
                    .filter(s -> Long.valueOf(s.get("event_id").getS()).equals(eventId))
                    .map(s -> Double.valueOf(s.get(columnName).getN()))
                    .toList().get(0);

        } catch (AmazonServiceException e) {

            log.error("Found an error: {}. Be sure that it exists and that you've typed its name correctly!", e.getErrorMessage());
            System.exit(1);
            return -1.0;
        }

    }

    public Double getAwayOddsForEvent(Long eventId) {

        return getDynamoColumnValueForEvent("away_odds", eventId);

    }

    public Double getDblChanceAwayOddsForEvent(Long eventId) {

        return getDynamoColumnValueForEvent("double_chance_away_odds", eventId);

    }

    public Double getDblChanceHomeOddsForEvent(Long eventId) {

        return getDynamoColumnValueForEvent("double_chance_home_odds", eventId);

    }


}
