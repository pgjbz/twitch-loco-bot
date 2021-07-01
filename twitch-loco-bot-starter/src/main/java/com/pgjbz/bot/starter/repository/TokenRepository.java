package com.pgjbz.bot.starter.repository;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;

import java.util.Optional;

public interface TokenRepository extends Repository<Token, TokenPk> {

    Optional<Token> findByPk(TokenPk tokenPk);
    boolean update(Token token);

}
