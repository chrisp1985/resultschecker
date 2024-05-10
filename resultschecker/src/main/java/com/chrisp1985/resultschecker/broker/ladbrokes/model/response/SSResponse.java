
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class SSResponse {

    public String xmlns;
    public List<Child> children;

}
