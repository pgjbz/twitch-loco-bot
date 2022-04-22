package com.pgjbz.bot.starter.chain;


import static com.pgjbz.bot.starter.configs.BotConstants.TOKEN_UNIT_ADD;
import static java.util.Objects.nonNull;

import java.util.List;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class AddUnitTokenChain extends AbstractTokenChain{

    private final TokenService tokenService;

    @Override
    public void doAddUnits(List<Token> tokens) {
        tokens.forEach(
            token -> {
                String username = token.getPk().getUsername();
                log.info("Add {} token units for user {} on channel {}", TOKEN_UNIT_ADD, username, token.getPk().getChannel());
                token.increaseTokenUnit();
            }    
        );
        try {

            tokenService.update(tokens);
        } catch (Exception e) {
            log.error("Error on add tokens units ", tokens.toString());
            return;
        }
        if(nonNull(next))
            next.doAddUnits(tokens);
    }
}
