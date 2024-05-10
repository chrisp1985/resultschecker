
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class EventIncident {

    public String id;
    public String eventId;
    public String eventPeriodId;
    public String eventParticipantId;
    public String incidentCode;
    public String description;
    public String relativeTime;
    public String createDate;
    public List<Child__4> children;

}
