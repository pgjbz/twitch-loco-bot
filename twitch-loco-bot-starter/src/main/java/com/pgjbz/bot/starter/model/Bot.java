package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@ToString
public class Bot {

    private Long id;
    private final String name;

    public Bot(String name) {
        if(isBlank(name))
            throw new IllegalArgumentException("Name is mandatory");
        this.name = name;
    }

    public Bot(Long id, String name) {
        if(isNull(id) || isBlank(name))
            throw new IllegalArgumentException("Name and id is mandatory");
        this.id = id;
        this.name = name;
    }

}
