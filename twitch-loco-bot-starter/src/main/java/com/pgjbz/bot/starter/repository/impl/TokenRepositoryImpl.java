package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Log4j2
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(Token token) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO TOKENS( ");
        sql.append("   USERNAME, ");
        sql.append("   CHANNEL, ");
        sql.append("   UNIT) ");
        sql.append(" VALUES(?,?,?) ");
        return jdbcTemplate.update(sql.toString(), new Object[]{token.getPk().getUsername(), token.getPk().getChannel(), token.getUnit()}) > 0;
    }

    @Override
    public List<Token> findAll() {
        List<Token> tokens = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   USERNAME, ");
        sql.append("   CHANNEL, ");
        sql.append("   UNIT ");
        sql.append(" FROM ");
        sql.append("   TOKENS ");
        try {
            tokens = jdbcTemplate.query(sql.toString(), rowMapper);
        } catch (EmptyResultException e) {
            log.info("No results found");
        }
        return tokens;
    }

    @Override
    public Optional<Token> findByPk(TokenPk tokenPk) {
        Token token = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   USERNAME, ");
        sql.append("   CHANNEL, ");
        sql.append("   UNIT ");
        sql.append(" FROM ");
        sql.append("   TOKENS ");
        sql.append(" WHERE ");
        sql.append("   USERNAME = ? ");
        sql.append("   AND CHANNEL = ? ");
        try {
            token = jdbcTemplate.queryForObject(sql.toString(),
                    new Object[]{tokenPk.getUsername(), tokenPk.getChannel()},
                    rowMapper);
        } catch (EmptyResultException e) {
            log.info("No result found for pk username {} | channel {}",
                    tokenPk.getUsername(),
                    tokenPk.getChannel());
        }
        return ofNullable(token);
    }

    @Override
    public boolean update(Token token) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append("   TOKENS ");
        sql.append(" SET ");
        sql.append("   UNIT = ? ");
        sql.append(" WHERE ");
        sql.append("   USERNAME = ? ");
        sql.append("   AND CHANNEL = ? ");
        return jdbcTemplate.update(sql.toString(),
                new Object[]{token.getUnit(), token.getPk().getUsername(), token.getPk().getChannel()}) > 0;
    }

    private final RowMapper<Token> rowMapper = (rs, row)  -> {
        String username = rs.getString("USERNAME");
        String channel = rs.getString("CHANNEL");
        Long unit = rs.getLong("UNIT");
        return new Token(new TokenPk(username, channel), unit);
    };
}
