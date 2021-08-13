package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.BotResponse;
import com.pgjbz.bot.starter.repository.BotResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class BotResponseRepositoryImpl implements BotResponseRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(BotResponse botResponse) {
        String sql = "INSERT INTO BOT_TARGET_RESPONSES (MESSAGE) VALUES (?)";
        return jdbcTemplate.update(sql, new Object[]{botResponse.getMessage()}) > 0;
    }

    @Override
    public List<BotResponse> findAll() {
        List<BotResponse> botResponses = new ArrayList<>();
        String sql = "SELECT ID, MESSAGE FROM BOT_TARGET_RESPONSES";
        try {
            botResponses = jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultException e) {
            log.info("No bot responses founded");
        }
        return botResponses;
    }

    private final RowMapper<BotResponse> rowMapper = (rs, row) -> {
        Long id = rs.getLong("ID");
        String message = rs.getString("MESSAGE");
        return new BotResponse(id, message);
    };
}
