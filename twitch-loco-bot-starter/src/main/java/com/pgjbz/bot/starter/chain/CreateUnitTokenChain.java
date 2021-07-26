package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class CreateUnitTokenChain extends AbstractTokenChain{

    private final TokenService tokenService;

    @Override
    public void doAddUnits(Token token) {
        log.info("Checking user for token {}", token.toString());
        try {
            Optional<Token> optionalToken = tokenService.findByPk(token.getPk());
            if(optionalToken.isEmpty()) {
                log.info("Token not found inserting new token");
                tokenService.insert(token);
            } else {
                log.info("Token already exists change reference");
                token = optionalToken.get();
            }
        } catch (Exception e) {
            log.error("Error on insert new token {}", token.toString(), e);
            return;
        }
        if(nonNull(next))
            next.doAddUnits(token);
    }
}
