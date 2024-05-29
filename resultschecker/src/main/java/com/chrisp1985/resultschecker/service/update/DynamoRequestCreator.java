package com.chrisp1985.resultschecker.service.update;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.chrisp1985.resultschecker.service.read.DynamoReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Component
public class DynamoRequestCreator {

    DynamoReadService readService;

    @Autowired
    public DynamoRequestCreator(DynamoReadService readService) {
        this.readService = readService;
    }

    public HashMap<String, AttributeValueUpdate> createUpdatedValuesMap(Integer homeScore,
                                                                        Integer awayScore,
                                                                        Long eventId) {
        double away_value = this.getAwayOddsReturnForFivePounds(homeScore, awayScore, eventId);
        double dbl_chance_away_value = this.getAwayDblChanceOddsReturnForFivePounds(homeScore, awayScore, eventId);
        double dbl_chance_home_value = this.getHomeDblChanceOddsReturnForFivePounds(homeScore, awayScore, eventId);


        HashMap<String, AttributeValueUpdate> updated_values =
                new HashMap<String, AttributeValueUpdate>();
        updated_values.put("home_goals_result",
                new AttributeValueUpdate(new AttributeValue().withN(String.valueOf(homeScore)), AttributeAction.PUT));
        updated_values.put("away_goals_result",
                new AttributeValueUpdate(new AttributeValue().withN(String.valueOf(awayScore)), AttributeAction.PUT));
        updated_values.put("results_timestamp",
                new AttributeValueUpdate(new AttributeValue(getTimestampToAddToDb()), AttributeAction.PUT));
        updated_values.put("fiver_on_double_chance_away",
                new AttributeValueUpdate(new AttributeValue().withN(String.valueOf(dbl_chance_away_value)), AttributeAction.PUT));
        updated_values.put("fiver_on_double_chance_home",
                new AttributeValueUpdate(new AttributeValue().withN(String.valueOf(dbl_chance_home_value)), AttributeAction.PUT));

        return updated_values;

    }



    public Double getAwayOddsReturnForFivePounds(Integer homeScore, Integer awayScore, Long eventId) {

        double moneyStakedPounds = 5.0;

        return awayScore > homeScore ? moneyStakedPounds * readService.getAwayOddsForEvent(eventId) : -moneyStakedPounds;

    }

    public Double getAwayDblChanceOddsReturnForFivePounds(Integer homeScore, Integer awayScore, Long eventId) {

        double moneyStakedPounds = 5.0;

        return awayScore >= homeScore ? moneyStakedPounds * readService.getDblChanceAwayOddsForEvent(eventId) : -moneyStakedPounds;

    }

    public Double getHomeDblChanceOddsReturnForFivePounds(Integer homeScore, Integer awayScore, Long eventId) {

        double moneyStakedPounds = 5.0;

        return awayScore <= homeScore ? moneyStakedPounds * readService.getDblChanceHomeOddsForEvent(eventId) : -moneyStakedPounds;

    }

    String getTimestampToAddToDb() {

        String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(datePattern);

        return europeanDateFormatter.format(LocalDateTime.now().atOffset(ZoneOffset.UTC)) + ".000Z";

    }
}
