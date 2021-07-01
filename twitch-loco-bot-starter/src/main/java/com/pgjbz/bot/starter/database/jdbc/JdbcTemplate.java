package com.pgjbz.bot.starter.database.jdbc;

import lombok.NonNull;

import java.util.List;

public interface JdbcTemplate {

    int update(@NonNull String sql, @NonNull Object[] params);
    int update(String sql);
    <T> T queryForObject(@NonNull String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper);
    <T> List<T> query(String sql, @NonNull Object[] params, @NonNull RowMapper<T> rowMapper);
    <T> List<T> query(String sql, RowMapper<T> rowMapper);
}
