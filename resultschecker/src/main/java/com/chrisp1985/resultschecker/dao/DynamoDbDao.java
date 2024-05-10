package com.chrisp1985.resultschecker.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.document.ScanFilter;
import com.amazonaws.services.dynamodbv2.model.*;
import com.chrisp1985.resultschecker.configuration.DynamoClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class DynamoDbDao implements StorageDao {
    private static final String DYNAMODB_TABLE_NAME = "FootballResults_tf";

    private final DynamoClientConfig dynamoClientConfig;

    @Autowired
    public DynamoDbDao(DynamoClientConfig dynamoClientConfig) {
        this.dynamoClientConfig = dynamoClientConfig;

    }

    public void update(Map<String, AttributeValue> key,
                       Map<String, AttributeValueUpdate> attributeUpdates) {
        try {

            dynamoClientConfig.getDdbClient().updateItem(DYNAMODB_TABLE_NAME, key, attributeUpdates);

        } catch (AmazonServiceException e) {

            log.error("Found an error: {}. Be sure that it exists and that you've typed its name correctly!", e.getErrorMessage());
            System.exit(1);

        }
    }

    public ScanResult read(HashMap<String, Condition> resultsFilter) {

        ScanRequest request = new ScanRequest(DYNAMODB_TABLE_NAME);
        request.withScanFilter(resultsFilter);
        return dynamoClientConfig.getDdbClient().scan(request);

    }

    public void delete(Map<String, AttributeValue> key) {
        DeleteItemRequest deleteReq = new DeleteItemRequest()
                .withTableName(DYNAMODB_TABLE_NAME)
                .withKey(key);
        dynamoClientConfig.getDdbClient().deleteItem(deleteReq);
    }
}
