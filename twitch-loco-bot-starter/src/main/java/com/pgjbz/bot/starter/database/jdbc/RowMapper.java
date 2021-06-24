package com.pgjbz.bot.starter.database.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {

    T rowMap(ResultSet rs, long line) throws SQLException;

}
