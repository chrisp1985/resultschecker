package com.chrisp1985.resultschecker;

import com.chrisp1985.resultschecker.broker.ladbrokes.model.response.ResponseModel;
import com.chrisp1985.resultschecker.broker.ladbrokes.query.LadbrokesResponseParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class ReadServiceTests {

    LadbrokesResponseParser ladbrokesResponseParser;
    ObjectMapper objectMapper;
    ResponseModel responseModel;
    String responseJson = "{\n" +
            "  \"SSResponse\": {\n" +
            "    \"xmlns\": \"http://schema.openbet.com/SiteServer/2.31/SSResponse.xsd\",\n" +
            "    \"children\": [\n" +
            "      {\n" +
            "        \"event\": {\n" +
            "          \"id\": \"244678200\",\n" +
            "          \"name\": \"Austria Salzburg v SCR Altach II\",\n" +
            "          \"eventStatusCode\": \"S\",\n" +
            "          \"displayOrder\": \"0\",\n" +
            "          \"siteChannels\": \"p,P,Q,R,C,I,M,\",\n" +
            "          \"eventSortCode\": \"MTCH\",\n" +
            "          \"startTime\": \"2024-05-11T15:00:00Z\",\n" +
            "          \"rawIsOffCode\": \"N\",\n" +
            "          \"isStarted\": null,\n" +
            "          \"isResulted\": \"true\",\n" +
            "          \"isFinished\": \"true\",\n" +
            "          \"classId\": \"74\",\n" +
            "          \"typeId\": \"126649\",\n" +
            "          \"sportId\": \"16\",\n" +
            "          \"liveServChannels\": \"sEVENT0244678200,\",\n" +
            "          \"liveServChildrenChannels\": \"SEVENT0244678200,\",\n" +
            "          \"categoryId\": \"16\",\n" +
            "          \"categoryCode\": \"FOOTBALL\",\n" +
            "          \"categoryName\": \"Football\",\n" +
            "          \"categoryDisplayOrder\": \"-11100\",\n" +
            "          \"className\": \"Austrian\",\n" +
            "          \"classDisplayOrder\": \"-32503\",\n" +
            "          \"classSortCode\": \"FB\",\n" +
            "          \"typeName\": \"Regionalliga West\",\n" +
            "          \"typeDisplayOrder\": \"-2100\",\n" +
            "          \"typeFlagCodes\": \"IVA,PVA,\",\n" +
            "          \"drilldownTagNames\": \"EVFLAG_BL,\",\n" +
            "          \"cashoutAvail\": \"Y\",\n" +
            "          \"children\": [\n" +
            "            {\n" +
            "              \"eventPeriod\": {\n" +
            "                \"id\": \"12482621\",\n" +
            "                \"eventId\": \"244678200\",\n" +
            "                \"periodCode\": \"ALL\",\n" +
            "                \"description\": \"Total Duration of the game/match\",\n" +
            "                \"startTime\": \"2024-05-11T17:29:13Z\",\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"eventPeriodClockState\": {\n" +
            "                      \"id\": \"5780071\",\n" +
            "                      \"eventPeriodId\": \"12482621\",\n" +
            "                      \"offset\": \"0\",\n" +
            "                      \"lastUpdate\": \"2024-05-11T17:29:13Z\",\n" +
            "                      \"state\": \"S\"\n" +
            "                    },\n" +
            "                    \"eventFact\": null,\n" +
            "                    \"eventPeriod\": null\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              \"eventParticipant\": null\n" +
            "            },\n" +
            "            {\n" +
            "              \"eventPeriod\": null,\n" +
            "              \"eventParticipant\": {\n" +
            "                \"id\": \"2214342\",\n" +
            "                \"eventId\": \"244678200\",\n" +
            "                \"name\": \"Austria Salzburg\",\n" +
            "                \"type\": \"T\",\n" +
            "                \"roleCode\": \"HOME\",\n" +
            "                \"role\": \"Home Team\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"eventPeriod\": null,\n" +
            "              \"eventParticipant\": {\n" +
            "                \"id\": \"2214343\",\n" +
            "                \"eventId\": \"244678200\",\n" +
            "                \"name\": \"SCR Altach II\",\n" +
            "                \"type\": \"T\",\n" +
            "                \"roleCode\": \"AWAY\",\n" +
            "                \"role\": \"Away Team\"\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        \"responseFooter\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"event\": null,\n" +
            "        \"responseFooter\": {\n" +
            "          \"cost\": \"9\",\n" +
            "          \"creationTime\": \"2024-05-12T19:41:13.250Z\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @BeforeEach
    void resetObject() throws JsonProcessingException {
        ladbrokesResponseParser = new LadbrokesResponseParser();
        responseModel = new ObjectMapper().readValue(responseJson, ResponseModel.class);
        ladbrokesResponseParser.setResponse(responseModel);
    }

    @Test
    void isResultedTrueIsValid() {
        Assert.isTrue(ladbrokesResponseParser.isResulted(), "isResulted failed.");
    }

    @Test
    void isResultedFalseIsValid() {
        responseModel.SSResponse.children.get(0).event.setIsResulted(false);
        Assert.isTrue(!ladbrokesResponseParser.isResulted(), "isResulted failed.");
    }
}
