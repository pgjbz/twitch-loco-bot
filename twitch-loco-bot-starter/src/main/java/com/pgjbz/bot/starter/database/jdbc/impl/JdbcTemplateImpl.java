package com.pgjbz.bot.starter.database.jdbc.impl;

import com.pgjbz.bot.starter.database.DB;
import com.pgjbz.bot.starter.database.exception.DatabaseException;
import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.exception.MoreThanOneException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JdbcTemplateImpl implements JdbcTemplate {

    public boolean update(@NonNull String sql, @NonNull Object[] params) {
        if(isBlank(sql))
            throw new IllegalArgumentException("SQL cannot be empty");
        try(Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            for(int i = 0; i < params.length; i++)
                ps.setObject(i+1, params[i]);
            return ps.executeUpdate() > 1;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public boolean update(String sql) {
        return update(sql, new Object[]{});
    }

    public <T> T findObject(@NonNull String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper) {
        try(Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public <T> List<T> findAll(String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper) {
        try(Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public <T> List<T> findAll(String sql, RowMapper<T> rowMapper) {
        return findAll(sql, new Object[]{}, rowMapper);
    }

}
