package com.pgjbz.bot.starter.model;

import com.pgjbz.bot.starter.configs.BotConstants;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import static java.util.Objects.isNull;

@Getter
@ToString
public class Token {

    private final TokenPk pk;
    private Long unit;

    public Token(@NonNull TokenPk pk, Long unit) {
        this.pk = pk;
        this.unit = unit;
    }

    public void increaseTokenUnit() {
        if(isNull(unit)) {
            unit = BotConstants.TOKEN_UNIT_ADD;
            return;
        }
        unit += BotConstants.TOKEN_UNIT_ADD;
    }

}
