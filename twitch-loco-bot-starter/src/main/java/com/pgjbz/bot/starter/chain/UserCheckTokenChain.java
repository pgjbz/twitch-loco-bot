package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class UserCheckTokenChain extends AbstractTokenChain{

    private final UserService userService;

    @Override
    public void doAddUnits(Token token) {
        String username = token.getPk().getUsername();
        log.info("Perform user validation for user {}", username);
        try {
            userService.saveIfNotExists(username);
        } catch (Exception e) {
            log.error("Error on check user {}", username, e);
            return;
        }
        if(nonNull(next))
            next.doAddUnits(token);
    }
}
