package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Bot;
import com.pgjbz.bot.starter.repository.BotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class BotRepositoryImpl implements BotRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(Bot bot) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO ");
        sql.append("   BOTS( ");
        sql.append("      BOT_NAME) ");
        sql.append(" VALUES ");
        sql.append("   (?) ");
        return jdbcTemplate.update(sql.toString(), new Object[]{bot.getName()}) > 0;
    }

    @Override
    public List<Bot> findAll() {
        log.info("Search all bots");
        List<Bot> bots = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   ID, ");
        sql.append("   BOT_NAME ");
        sql.append(" FROM ");
        sql.append("   BOTS ");
        try {
            bots = jdbcTemplate.query(sql.toString(), rowMapper);
        } catch (EmptyResultException e) {
            log.info("No results founded for bots");
        }
        return bots;
    }

    private final RowMapper<Bot> rowMapper = (rs, row) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("bot_name");
        return new Bot(id, name);
    };
}
