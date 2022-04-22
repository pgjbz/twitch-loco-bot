package com.pgjbz.bot.starter.chain;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class CreateUnitTokenChain extends AbstractTokenChain {

    private final TokenService tokenService;

    @Override
    public void doAddUnits(List<Token> tokens) {
        log.info("Checking user for token {}", tokens.toString());
        try {
            tokens = tokens.stream().map(
                    token -> {
                        Optional<Token> optionalToken = tokenService.findByPk(token.getPk());
                        if (optionalToken.isEmpty()) {
                            log.info("Token not found inserting new token");
                            tokenService.insert(token);
                            return null;
                        }
                        log.info("Token already exists change reference");
                        return optionalToken.get();
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error on insert new token {}", tokens.toString(), e);
            return;
        }
        if (nonNull(next))
            next.doAddUnits(tokens);
    }
}
