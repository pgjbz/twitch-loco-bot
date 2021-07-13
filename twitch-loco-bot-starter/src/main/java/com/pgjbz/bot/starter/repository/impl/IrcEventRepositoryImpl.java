package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.repository.IrcEventRepository;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class IrcEventRepositoryImpl implements IrcEventRepository {

    private final JdbcTemplate  jdbcTemplate;

    @Override
    public boolean insert(IrcEvent ircEvent) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO IRC_EVENT( ");
        sql.append("   USERNAME, ");
        sql.append("   CHANNEL, ");
        sql.append("   UNKNOWN_EVENT_STRING, ");
        sql.append("   EVENT_RECEIVE, ");
        sql.append("   KEYS, ");
        sql.append("   EVENT_DATE) ");
        sql.append(" VALUES (?,?,?,?,?, CURRENT_TIMESTAMP) ");
        return jdbcTemplate.update(sql.toString(), new Object[]{ircEvent.getUsername(),
                ircEvent.getChannel(),
                ircEvent.getUnknownString(),
                ircEvent.getCommandReceive().name(),
                ircEvent.getKeys().toString()}) > 0;
    }

}
