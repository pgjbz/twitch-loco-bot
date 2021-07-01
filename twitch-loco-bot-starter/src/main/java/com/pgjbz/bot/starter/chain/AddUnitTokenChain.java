package com.pgjbz.bot.starter.chain;


import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static com.pgjbz.bot.starter.configs.BotConstants.TOKEN_UNIT_ADD;
import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class AddUnitTokenChain extends AbstractTokenChain{

    private final TokenRepository tokenRepository;

    @Override
    public void doAddUnits(Token token) {
        String username = token.getPk().getUsername();
        log.info("Add {} token units for user {}", TOKEN_UNIT_ADD, username);
        try {
            token.increaseTokenUnit();
            tokenRepository.update(token);
        } catch (Exception e) {
            log.error("Error on add token unit from user {} in channel {}", token.getPk().getUsername(), token.getPk().getChannel());
            return;
        }
        if(nonNull(next))
            next.doAddUnits(token);
    }
}
