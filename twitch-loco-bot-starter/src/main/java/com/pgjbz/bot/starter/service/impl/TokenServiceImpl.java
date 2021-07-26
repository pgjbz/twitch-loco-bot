package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.repository.TokenRepository;
import com.pgjbz.bot.starter.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class TokenServiceImpl  implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public boolean insert(Token token) {
        boolean inserted = false;
        try {
            inserted = tokenRepository.insert(token);
        } catch (Exception e) {
            log.error("Error on insert new token {}", token.toString(), e);
        }
        return inserted;
    }

    @Override
    public List<Token> findAll() {
        List<Token> tokens = new ArrayList<>();
        try {
            tokens =  tokenRepository.findAll();
        } catch (Exception e) {
            log.error("Error on fina all tokens", e);
        }
        return tokens;
    }

    @Override
    public Optional<Token> findByPk(TokenPk tokenPk) {
        Optional<Token> optionalToken = Optional.empty();
        try {
            optionalToken =  tokenRepository.findByPk(tokenPk);
        } catch (Exception e){
            log.error("Error on find token by pk {}", tokenPk.toString(), e);
        }
        return  optionalToken;
    }

    @Override
    public boolean update(Token token) {
        boolean updated = false;
        try {
            updated = tokenRepository.update(token);
        } catch (Exception e) {
            log.error("Error on update token {}", token.toString(), e);
        }
        return updated;
    }
}
