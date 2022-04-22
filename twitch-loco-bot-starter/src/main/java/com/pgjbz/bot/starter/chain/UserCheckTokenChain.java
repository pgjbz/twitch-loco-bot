package com.pgjbz.bot.starter.chain;

import static java.util.Objects.nonNull;

import java.util.List;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.bot.starter.util.BotUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class UserCheckTokenChain extends AbstractTokenChain{

    private final UserService userService;

    @Override
    public void doAddUnits(List<Token> tokens) {

        tokens.stream().filter(token -> {
            String username = token.getPk().getUsername();
            if(BotUtils.isBot(username)) {
                log.info("User is bot {}... skipping", username);
                return false;
            }
            return true;
        }).forEach(
            token -> {
                var username = token.getPk().getUsername();
                log.info("Perform user validation for user {}", username);
                try {
                    userService.saveIfNotExists(username);
                } catch (Exception e) {
                    log.error("Error on check user {}", username, e);
                    return;
                }
            }
        );

        if(nonNull(next))
            next.doAddUnits(tokens);
    }
}
