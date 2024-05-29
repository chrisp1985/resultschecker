package com.chrisp1985.resultschecker.service.read;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Component
public class DynamoReadFilter {


    HashMap<String, Condition> resultsWithNoTimestampFilter() {

        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition resultsTimestampEmpty = getFilterCondition(ComparisonOperator.EQ.toString(), "EMPTY");
        Condition startTimeGreaterThanOneDay = getFilterCondition(ComparisonOperator.NOT_CONTAINS.toString(), getDateToday());

        scanFilter.put("results_timestamp", resultsTimestampEmpty);
        scanFilter.put("start_time", startTimeGreaterThanOneDay);
        return scanFilter;

    }

    private Condition getFilterCondition(String comparisonOperator, String attributeValue) {
        return new Condition()
                .withComparisonOperator(
                        comparisonOperator)
                .withAttributeValueList(
                        new AttributeValue(attributeValue));
    }

    public static String getDateToday() {
        String datePattern = "yyyy-MM-dd'T'";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(datePattern);
        return europeanDateFormatter.format(LocalDateTime.now().atOffset(ZoneOffset.UTC));
    }
}
