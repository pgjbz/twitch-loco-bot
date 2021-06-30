package com.pgjbz.twitch.loco.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class StreamInfo {

    @JsonProperty(value = "chatter_count")
    private Integer chatterCounter;
    private Chatters chatters;

}
