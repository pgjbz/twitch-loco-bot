package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Log4j2
@RequiredArgsConstructor
public class TwitchUserRepositoryImpl implements TwitchUserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(TwitchUser twitchUser) {
        String sql = " INSERT INTO USERS(USERNAME, JOIN_DATE) VALUES (?, current_timestamp) ";
        return jdbcTemplate.update(sql, new Object[]{twitchUser.getUsername()}) > 0 ;
    }

    @Override
    public List<TwitchUser> findAll() {
        String sql = " SELECT ID, USERNAME, JOIN_DATE FROM USERS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<TwitchUser> findByUsername(String username) {
        TwitchUser twitchUser = null;
        String sql = " SELECT USERNAME, JOIN_DATE FROM USERS WHERE USERNAME = ?";
        try {
            twitchUser = jdbcTemplate.queryForObject(sql, new Object[]{username}, rowMapper);
        } catch (EmptyResultException e){
            log.warn("Username {} not found", username, e);
        }
        return ofNullable(twitchUser);
    }

    private static final RowMapper<TwitchUser> rowMapper = (rs, rowNum) ->
       new TwitchUser(rs.getString("username"), rs.getDate("join_date"));

}
