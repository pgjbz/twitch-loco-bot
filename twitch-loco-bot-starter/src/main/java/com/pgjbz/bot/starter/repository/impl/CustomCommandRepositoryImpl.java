package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.CustomCommand;
import com.pgjbz.bot.starter.repository.CustomCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class CustomCommandRepositoryImpl implements CustomCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(CustomCommand customCommand) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO CUSTOM_COMMANDS ( ");
        sql.append("   CREATED_AT, ");
        sql.append("   USE_COUNT, ");
        sql.append("   ONLY_MODS, ");
        sql.append("   COMMAND_MESSAGE, ");
        sql.append("   CHANNEL, ");
        sql.append("   COMMAND, ");
        sql.append("   TOKEN_COST, ");
        sql.append("   CREATED_BY) ");
        sql.append(" VALUES(CURRENT_TIMESTAMP, 0, ?, ?, ?, ?, ?, ?) ");
        return jdbcTemplate.update(sql.toString(), new Object[]{
                customCommand.getOnlyMods(),
                customCommand.getCommandMessage(),
                customCommand.getChannel(),
                customCommand.getCommand(),
                customCommand.getTokenCost(),
                customCommand.getCreatedBy()
        }) > 0;
    }

    @Override
    public Optional<CustomCommand> findByChannelAndCommand(String channel, String command) {
        CustomCommand customCommand = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   ID, ");
        sql.append("   CREATED_AT, ");
        sql.append("   UPDATED_AT, ");
        sql.append("   ONLY_MODS, ");
        sql.append("   COMMAND, ");
        sql.append("   COMMAND_MESSAGE, ");
        sql.append("   CHANNEL, ");
        sql.append("   CREATED_BY, ");
        sql.append("   TOKEN_COST, ");
        sql.append("   USE_COUNT ");
        sql.append(" FROM ");
        sql.append("   CUSTOM_COMMANDS ");
        sql.append(" WHERE ");
        sql.append("   CHANNEL = ? ");
        sql.append("   AND COMMAND = ? ");
        try {
            customCommand = jdbcTemplate.queryForObject(sql.toString(), new Object[] {
                    channel, command
            }, rowMapper);
        } catch (EmptyResultException e) {
            log.warn("Command {} on channel {} not found", command, channel);
        }
        return Optional.ofNullable(customCommand);
    }

    @Override
    public boolean update(CustomCommand command) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE CUSTOM_COMMANDS SET ");
        sql.append("   COMMAND_MESSAGE = ?, ");
        sql.append("   TOKEN_COST = ?, ");
        sql.append("   UPDATED_AT = CURRENT_TIMESTAMP, ");
        sql.append("   USE_COUNT = ? ");
        sql.append(" WHERE ");
        sql.append("   COMMAND = ? ");
        sql.append("   AND CHANNEL = ? ");
        return jdbcTemplate.update(sql.toString(), new Object[]{
                command.getCommandMessage(),
                command.getTokenCost(),
                command.getUseCount(),
                command.getCommand(),
                command.getChannel()
        }) > 0;
    }

    @Override
    public boolean delete(CustomCommand command) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM ");
        sql.append("   CUSTOM_COMMANDS ");
        sql.append(" WHERE ");
        sql.append("   COMMAND = ? ");
        sql.append("   AND CHANNEL = ? ");
        return jdbcTemplate.update(sql.toString(),
                new Object[]{command.getCommand(),
                        command.getChannel()}) > 0;
    }

    @Override
    public List<CustomCommand> findByChannel(String channel) {
        List<CustomCommand> commands = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   ID, ");
        sql.append("   CREATED_AT, ");
        sql.append("   UPDATED_AT, ");
        sql.append("   ONLY_MODS, ");
        sql.append("   COMMAND, ");
        sql.append("   COMMAND_MESSAGE, ");
        sql.append("   CHANNEL, ");
        sql.append("   CREATED_BY, ");
        sql.append("   TOKEN_COST, ");
        sql.append("   USE_COUNT ");
        sql.append(" FROM ");
        sql.append("   CUSTOM_COMMANDS ");
        sql.append(" WHERE ");
        sql.append("   CHANNEL = ? ");
        try {
            commands = jdbcTemplate.query(sql.toString(), new Object[]{
                    channel
            }, rowMapper);
        } catch (EmptyResultException e) {
            log.info("No commands founded");
        }
        return commands;
    }

    @Override
    public List<CustomCommand> findAll() {
        List<CustomCommand> commands = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   ID, ");
        sql.append("   CREATED_AT, ");
        sql.append("   UPDATED_AT, ");
        sql.append("   ONLY_MODS, ");
        sql.append("   COMMAND, ");
        sql.append("   COMMAND_MESSAGE, ");
        sql.append("   CHANNEL, ");
        sql.append("   CREATED_BY, ");
        sql.append("   TOKEN_COST, ");
        sql.append("   USE_COUNT ");
        sql.append(" FROM ");
        sql.append("   CUSTOM_COMMANDS ");
        try {
            commands = jdbcTemplate.query(sql.toString(), rowMapper);
        } catch (EmptyResultException e) {
            log.info("No commands founded");
        }
        return commands;
    }

    private final RowMapper<CustomCommand> rowMapper = (rs, rowNum) -> {
        String command = rs.getString("COMMAND");
        String commandMessage = rs.getString("COMMAND_MESSAGE");
        String channel = rs.getString("CHANNEL");
        String createdBy = rs.getString("CREATED_BY");
        Long useCount = rs.getLong("USE_COUNT");
        Long id = rs.getLong("ID");
        Long tokenCost = rs.getLong("TOKEN_COST");
        Boolean onlyMods = rs.getBoolean("ONLY_MODS");
        Date createdAt = rs.getDate("CREATED_AT");
        Date updatedAt = rs.getDate("UPDATED_AT");
        CustomCommand customCommand = new CustomCommand(id,
                createdAt,
                createdBy,
                channel,
                onlyMods,
                tokenCost,
                command,
                useCount);
        customCommand.setUpdatedAt(updatedAt);
        customCommand.setCommandMessage(commandMessage);
        return customCommand;
    };
}
