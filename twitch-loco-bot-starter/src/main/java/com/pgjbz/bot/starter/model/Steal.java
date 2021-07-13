package com.pgjbz.bot.starter.model;

import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
public class Steal {

    private Long id;
    private final String steal;

    public Steal(String steal) {
        if(isBlank(steal))
            throw new IllegalArgumentException("Steal is mandatory");
        this.steal = steal;
    }

    public Steal(Long id, String steal) {
        this(steal);
        this.id = id;
    }

}
