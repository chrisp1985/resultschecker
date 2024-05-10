package com.chrisp1985.resultschecker.broker.ladbrokes.query;

import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.Child__2;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.Child;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class LadbrokesResponseParser {

    private final Integer homeIndex = 0;
    private final Integer awayIndex = 1;

    private List<Child> getEventChildrenFromResponse(ResponseModel response) {
        List<Child> emptyList = new ArrayList<>();
        return response.SSResponse.children.isEmpty() ? emptyList:
                response.SSResponse.children
                        .stream()
                        .filter(mainChild -> Objects.nonNull(mainChild.event))
                        .toList();
    }

    private Integer getScoreFromEvent(ResponseModel response, Integer index) {
        Map<Long, Integer> scores = new HashMap<>();
        List<Long> event_participants = new ArrayList<>();

        List<Child__2> children = getEventChildrenFromResponse(response).get(0).event.children.get(0).eventPeriod.getChildren();

        children.stream()
                .filter(this::isFact)
                .filter(child -> child.eventFact.factCode.equals("SCORE"))
                .forEach(child -> {
                    scores.put(child.eventFact.eventParticipantId, child.eventFact.fact);
                    event_participants.add(child.eventFact.eventParticipantId);
                });

        Collections.sort(event_participants);
        return scores.get(event_participants.get(index));
    }

    public Integer getHomeScoreForEvent(ResponseModel response) {
        return getScoreFromEvent(response, homeIndex);
    }

    public Integer getAwayScoreForEvent(ResponseModel response) {
        return getScoreFromEvent(response, awayIndex);
    }

    private boolean isFact(Child__2 childEvent) {

        try {
            return childEvent.eventFact.fact !=null;
        }
        catch (Exception e) {
            return false;
        }

    }

    public boolean isResulted(ResponseModel response) {

        try {
            return getEventChildrenFromResponse(response).get(0).event.isResulted != null;
        }
        catch (Exception e) {
            return false;
        }

    }




}
