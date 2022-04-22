package com.pgjbz.bot.starter.database.jdbc.impl;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.pgjbz.bot.starter.database.DB;
import com.pgjbz.bot.starter.database.exception.DatabaseException;
import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.exception.MoreThanOneException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.database.pool.BotDatabaseConnection;

import lombok.NonNull;

public class JdbcTemplateImpl implements JdbcTemplate {

    public int update(@NonNull String sql, @NonNull Object[] params) {
        if(isBlank(sql))
            throw new IllegalArgumentException("SQL cannot be empty");
        try(BotDatabaseConnection botConnection = DB.getConnection();
            PreparedStatement ps = botConnection.getConnection().prepareStatement(sql)) {
            for(int i = 0; i < params.length; i++)
                ps.setObject(i+1, params[i]);
            return ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public int update(String sql) {
        return update(sql, new Object[]{});
    }

    public <T> T queryForObject(@NonNull String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper) {
        try(BotDatabaseConnection botConnection = DB.getConnection();
            PreparedStatement ps = botConnection.getConnection().prepareStatement(sql)) {
            for(int i = 0; i < params.length; i++)
                ps.setObject(i+1, params[i]);
            T t = null;
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.getFetchSize() < 0)
                    throw new EmptyResultException("No database result");
                else if(rs.getFetchSize() > 0)
                    throw new MoreThanOneException("More than one object founded, use primary key to find unique object");
                if(rs.next())
                    t =  rowMapper.rowMap(rs, 1);
            }
            return t;
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public <T> List<T> query(String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper) {
        try(BotDatabaseConnection botConnection = DB.getConnection();
            PreparedStatement ps = botConnection.getConnection().prepareStatement(sql)) {
            for(int i = 0; i < params.length; i++)
                ps.setObject(i+1, params[i]);
            List<T> list = new ArrayList<>();
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.getFetchSize() < 0)
                    throw new EmptyResultException("No database result");
                while(rs.next())
                    list.add(rowMapper.rowMap(rs, rs.getRow()));
            }
            return list;
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, new Object[]{}, rowMapper);
    }

    @Override
    public int batchUpdate(String sql, List<Object[]> params) {
        try(BotDatabaseConnection botConnection = DB.getConnection();
            PreparedStatement ps = botConnection.getConnection().prepareStatement(sql)) {
            int counter = 0;
            int inserted = 0;
            for(int i = 0; i < params.size(); i++) {
                Object[] param = params.get(i);
                for(int j = 0; j < param.length; j++)
                    ps.setObject(j+1, param[j]);
                ps.addBatch();
                if(counter == 1000 || counter == params.size() - 1){
                    inserted += IntStream.of(ps.executeBatch()).sum();
                    counter = 0;
                } else {
                    counter++;
                }
            }
            return inserted;
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}
