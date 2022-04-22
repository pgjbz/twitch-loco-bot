package com.pgjbz.bot.starter.service;

import java.util.List;
import java.util.Optional;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;

public interface TokenService extends Service<Token, TokenPk> {

    Optional<Token> findByPk(TokenPk tokenPk);
    boolean update(Token token);
    boolean update(List<Token> token);

}
