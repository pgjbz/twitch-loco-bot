package com.pgjbz.bot.starter.repository;

import java.util.List;
import java.util.Optional;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;

public interface TokenRepository extends Repository<Token, TokenPk> {

    Optional<Token> findByPk(TokenPk tokenPk);
    boolean update(Token token);
    boolean update(List<Token> token);

}
