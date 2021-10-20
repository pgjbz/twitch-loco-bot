package com.pgjbz.twitch.loco.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StreamInfo(
        @JsonProperty(value = "chatter_count")
        Integer chatterCounter, Chatters chatters) {
}
