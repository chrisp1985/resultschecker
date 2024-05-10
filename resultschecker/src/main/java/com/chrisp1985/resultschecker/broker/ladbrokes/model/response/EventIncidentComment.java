
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventIncidentComment {

    public String id;
    public String eventIncidentId;
    public String text;
    public String lang;

}
