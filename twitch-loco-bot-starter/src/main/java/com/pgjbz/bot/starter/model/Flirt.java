package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.ToString;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@ToString
public class Flirt {

    private Long id;
    private final String teaser;

    public Flirt(String teaser) {
        if(isBlank(teaser))
            throw new IllegalArgumentException("Teser is mandatory");
        this.teaser = teaser;
    }

    public Flirt(Long id, String teaser) {
        this(teaser);
        this.id = id;
    }
}
