package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.ToString;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@ToString
public class Joke {

    private Long id;
    private final String joke;

    public Joke(String joke) {
        if(isBlank(joke))
            throw new IllegalArgumentException("Joke is mandatory");
        this.joke = joke;
    }

    public Joke(Long id, String joke) {
        this(joke);
        this.id = id;
    }

}
