
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventParticipant {

    public String id;
    public String eventId;
    public String name;
    public String type;
    public String roleCode;
    public String role;

}
