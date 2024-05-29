package com.chrisp1985.resultschecker.broker.ladbrokes.query;

import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.Child__2;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.Child;
import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class LadbrokesResponseParser {

    ResponseModel responseModel;

    public void setResponse(ResponseModel responseModel) {
        this.responseModel = responseModel;
    }

    private List<Child> getEventChildrenFromResponse() {
        if(this.responseModel == null) {
            return List.of();
        }
        return this.responseModel.SSResponse.children.isEmpty() ? List.of():
                this.responseModel.SSResponse.children
                        .stream()
                        .filter(mainChild -> Objects.nonNull(mainChild.event))
                        .toList();
    }

    private Integer getScoreFromEventAtIndex(Integer index) {
        if(getEventChildrenFromResponse().isEmpty()) {
            return 999;
        }

        Map<Long, Integer> scores = new HashMap<>();
        List<Long> event_participants = new ArrayList<>();

        List<Child__2> children = getEventChildrenFromResponse().get(0).event.children.get(0).eventPeriod.getChildren();
        try {
            children.stream()
                    .filter(this::isEventFactPresent)
                    .filter(child -> child.eventFact.factCode.equals("SCORE"))
                    .forEach(child -> {
                        scores.put(child.eventFact.eventParticipantId, child.eventFact.fact);
                        event_participants.add(child.eventFact.eventParticipantId);
                    });

            Collections.sort(event_participants);
            return scores.get(event_participants.get(index));
        }
        catch (IndexOutOfBoundsException e) {
            log.info("Exception encountered: {}", e.getMessage());
            return 999;
        }
    }

    public Integer getHomeScoreForEvent() {
        Integer homeIndex = 0;
        return getScoreFromEventAtIndex(homeIndex);
    }

    public Integer getAwayScoreForEvent() {
        Integer awayIndex = 1;
        return getScoreFromEventAtIndex(awayIndex);
    }

    private boolean isEventFactPresent(Child__2 childEvent) {

        return Optional.ofNullable(childEvent.eventFact).isPresent();

    }

    public boolean isResulted() {

        return Optional.ofNullable(getEventChildrenFromResponse())
                .flatMap(children -> children.stream().findFirst())
                .map(child -> child.event.isResulted)
                .orElse(false);

//        try {
//            return getEventChildrenFromResponse().get(0).event.isResulted;
//        }
//        catch (Exception e) {
//            return false;
//        }

    }




}
