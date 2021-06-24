package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static java.util.Optional.ofNullable;

@Log
@RequiredArgsConstructor
public class TwitchUserRepositoryImpl implements TwitchUserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(TwitchUser twitchUser) {
        String sql = " INSERT INTO USERS(USERNAME, JOIN_DATE) VALUES (?, current_timestamp) ";
        return jdbcTemplate.update(sql, new Object[]{twitchUser.getUsername()});
    }

    @Override
    public List<TwitchUser> findAll() {
        String sql = " SELECT ID, USERNAME, JOIN_DATE FROM USERS";
        return jdbcTemplate.findAll(sql, rowMapper);
    }

    @Override
    public Optional<TwitchUser> findByUsername(String username) {
        TwitchUser twitchUser = null;
        String sql = " SELECT ID, USERNAME, JOIN_DATE FROM USERS WHERE USERNAME = ?";
        try {
            twitchUser = jdbcTemplate.findObject(sql, new Object[]{username}, rowMapper);
        } catch (EmptyResultException e){
            log.log(Level.WARNING, String.format("Username %s not found", e));
        }
        return ofNullable(twitchUser);
    }

    private static final RowMapper<TwitchUser> rowMapper = (rs, rowNum) ->
       new TwitchUser(rs.getLong("id"), rs.getString("username"), rs.getDate("join_date"));

}
