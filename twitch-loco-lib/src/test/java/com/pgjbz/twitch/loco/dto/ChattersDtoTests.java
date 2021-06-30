package com.pgjbz.twitch.loco.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgjbz.twitch.loco.model.Chatters;
import org.junit.jupiter.api.Test;

public class ChattersDtoTests {

    @Test
    void testDeserializeResponseExpectedSuccess() throws Exception {
        String json = "{\n" +
                "  \"broadcaster\": [\n" +
                "    \"paulo97loco\"\n" +
                "  ],\n" +
                "  \"vips\": [\n" +
                "    \n" +
                "  ],\n" +
                "  \"moderators\": [\n" +
                "    \"paulo97lucido\"\n" +
                "  ],\n" +
                "  \"staff\": [\n" +
                "    \n" +
                "  ],\n" +
                "  \"admins\": [\n" +
                "    \n" +
                "  ],\n" +
                "  \"global_mods\": [\n" +
                "    \n" +
                "  ],\n" +
                "  \"viewers\": [\n" +
                "    \"paulo97loco\"\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Chatters chatter = mapper.readValue(json, Chatters.class);
        System.out.println(chatter.toString());
    }


}
