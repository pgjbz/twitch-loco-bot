package com.pgjbz.bot.starter.service;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;

import java.util.Optional;

public interface TokenService extends Service<Token, TokenPk> {

    Optional<Token> findByPk(TokenPk tokenPk);
    boolean update(Token token);

}
