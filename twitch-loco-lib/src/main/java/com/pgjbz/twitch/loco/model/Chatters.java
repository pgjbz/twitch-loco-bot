package com.pgjbz.twitch.loco.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class Chatters {

    private final List<String> broadcaster = new ArrayList<>();
    private final List<String> vips = new ArrayList<>();
    private final List<String> moderators = new ArrayList<>();
    private final List<String> staff = new ArrayList<>();
    private final List<String> admins = new ArrayList<>();
    @JsonProperty(value = "global_mods")
    private final List<String> globalMods = new ArrayList<>();
    private final List<String> viewers = new ArrayList<>();

}

