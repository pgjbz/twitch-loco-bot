package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static java.util.Objects.isNull;

@Getter
@ToString
public class CustomCommand {

    private final Long id;
    private final Date createdAt;
    @Setter
    private Date updatedAt;
    private Long useCount;
    private String commandMessage;
    private final String command;
    private final String createdBy;
    private final String channel;
    private Boolean onlyMods;
    private Long tokenCost;

    public CustomCommand(Long id,
                         @NonNull Date createdAt,
                         @NonNull String createdBy,
                         @NonNull String channel,
                         @NonNull Boolean onlyMods,
                         @NonNull Long tokenCost,
                         @NonNull String command,
                         @NonNull Long useCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.channel = channel;
        this.onlyMods = onlyMods;
        this.command = command;
        this.tokenCost = tokenCost;
        this.useCount = useCount;
    }

    public void incrementUseCount(){
        if(isNull(useCount)) {
            useCount = 1L;
            return;
        }
        useCount++;
    }

    public void setCommandMessage(String commandMessage) {
        if(StringUtils.isBlank(commandMessage))
            throw new IllegalArgumentException("Message is mandatory!");
        this.commandMessage = commandMessage;
    }

    public void setTokenCost(Long tokenCost) {
        if(isNull(tokenCost)) {
            this.tokenCost = 0L;
            return;
        }
        this.tokenCost = tokenCost;
    }

    public void setOnlyMods(Boolean onlyMods) {
        if(isNull(onlyMods)) {
            this.onlyMods = false;
            return;
        }
        this.onlyMods = onlyMods;
    }
}
