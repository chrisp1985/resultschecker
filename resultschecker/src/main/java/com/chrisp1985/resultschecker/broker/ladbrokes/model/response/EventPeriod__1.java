
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class EventPeriod__1 {

    public String id;
    public String eventId;
    public String parentEventPeriodId;
    public String periodCode;
    public String description;
    public String startTime;
    public List<Child__3> children;

}
