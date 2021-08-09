package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Ranking {

    private final String username;
    private final Long unit;
    private final Byte position;

    public Ranking(String username, @NonNull  Long unit, @NonNull Byte position) {
        if(StringUtils.isBlank(username))
            throw new IllegalArgumentException("Username is mandatory");
        this.username = username;
        this.unit = unit;
        this.position = position;
    }
}
