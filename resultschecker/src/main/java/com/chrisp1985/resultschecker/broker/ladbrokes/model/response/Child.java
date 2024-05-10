
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Child {

    public Event event;
    public ResponseFooter responseFooter;

}
