
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventPeriodClockState {

    public String id;
    public String eventPeriodId;
    public String offset;
    public String lastUpdate;
    public String state;

}
