package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Message;
import com.pgjbz.bot.starter.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(Message message) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO MESSAGES( ");
        sql.append("   USERNAME, ");
        sql.append("   MESSAGE, ");
        sql.append("   MESSAGE_DATE) ");
        sql.append(" VALUES(?,?, CURRENT_TIMESTAMP) ");
        return jdbcTemplate.update(sql.toString(), new Object[]{message.getUsername(), message.getMessage()}) > 0;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   ID, ");
        sql.append("   USERNAME, ");
        sql.append("   MESSAGE, ");
        sql.append("   MESSAGE_DATE ");
        sql.append(" FROM ");
        sql.append("   MESSAGES ");
        try {
            messages = jdbcTemplate.findAll(sql.toString(), rowMapper);
        } catch (EmptyResultException e) {
            log.log(Level.WARNING, "No registers founded");
        }
        return messages;
    }

    private static final RowMapper<Message> rowMapper = (rs, row) -> {
        Long id = rs.getLong("ID");
        String username =rs.getString("USERNAME");
        String message = rs.getString("MESSAGE");
        Date messageDate = new Date(rs.getDate("MESSAGE_DATE").getTime());
        return new Message(id, username, message, messageDate);
    };
}
