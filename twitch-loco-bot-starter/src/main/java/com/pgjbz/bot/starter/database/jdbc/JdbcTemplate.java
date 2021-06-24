package com.pgjbz.bot.starter.database.jdbc;

import lombok.NonNull;

import java.util.List;

public interface JdbcTemplate {

    boolean update(@NonNull String sql, @NonNull Object[] params);
    boolean update(String sql);
    <T> T findObject(@NonNull String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper);
    <T> List<T> findAll(String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper);
    <T> List<T> findAll(String sql, RowMapper<T> rowMapper);
}
