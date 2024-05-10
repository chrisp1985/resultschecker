
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventFact {

    public Long id;
    public Long eventId;
    public Long eventParticipantId;
    public Long eventPeriodId;
    public Integer fact;
    public String factCode;
    public String name;

}
