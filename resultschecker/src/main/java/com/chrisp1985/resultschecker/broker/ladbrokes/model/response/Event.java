
package com.chrisp1985.resultschecker.broker.ladbrokes.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
public class Event {

    public String id;
    public String name;
    public String eventStatusCode;
    public String displayOrder;
    public String siteChannels;
    public String eventSortCode;
    public String startTime;
    public String rawIsOffCode;
    public String isStarted;
    public String isResulted;
    public String isFinished;
    public String classId;
    public String typeId;
    public String sportId;
    public String liveServChannels;
    public String liveServChildrenChannels;
    public String categoryId;
    public String categoryCode;
    public String categoryName;
    public String categoryDisplayOrder;
    public String className;
    public String classDisplayOrder;
    public String classSortCode;
    public String typeName;
    public String typeDisplayOrder;
    public String typeFlagCodes;
    public String drilldownTagNames;
    public String cashoutAvail;
    public List<Child__1> children;

}
