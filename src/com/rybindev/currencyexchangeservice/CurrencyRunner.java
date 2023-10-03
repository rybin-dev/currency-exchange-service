package com.rybindev.currencyexchangeservice;

import com.rybindev.currencyexchangeservice.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrencyRunner {

    private static final String CREATE_TABLE_CURRENCY_SQL = """
            create table if not exists currency
            (
                id   integer primary key autoincrement,
                code varchar(16) not null,
                name varchar(32) not null,
                sing varchar(16) not null unique
            );
            """;
    private static final String CREATE_INDEX_CODE_SQL = """
            create unique index if not exists base_idx on exchange_rate(base_id,target_id);
            """;
    private static final String CREATE_TABLE_EXCHANGE_RATE_SQL = """
             create table if not exists exchange_rate
            (
                id   integer primary key autoincrement,
                base_id integer not null references currency(id) on delete cascade,
                target_id integer not null references currency(id) on delete cascade,
                rate decimal(6) not null  
            );
            """;
    private static final String CREATE_INDEX_BASE_ID_TARGET_ID_SQL = """
            create unique index if not exists base_id_and_target_id_idx on exchange_rate(base_id,target_id);
            """;

    public static void main(String[] args) throws SQLException {

        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch(CREATE_TABLE_CURRENCY_SQL);
            statement.addBatch(CREATE_TABLE_EXCHANGE_RATE_SQL);
            statement.addBatch(CREATE_INDEX_CODE_SQL);
            statement.addBatch(CREATE_INDEX_BASE_ID_TARGET_ID_SQL);
            statement.executeBatch();
        }

    }
}
