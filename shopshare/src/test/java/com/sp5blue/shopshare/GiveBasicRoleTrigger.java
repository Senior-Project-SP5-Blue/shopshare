package com.sp5blue.shopshare;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GiveBasicRoleTrigger implements Trigger {
    @Override
    public void init(Connection connection, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
        Trigger.super.init(connection, schemaName, triggerName, tableName, before, type);
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO roles(shopper_id) VALUES (?)")) {
            stmt.setObject(1, newRow[0]);
        }
    }

    @Override
    public void close() throws SQLException {
        Trigger.super.close();
    }

    @Override
    public void remove() throws SQLException {
        Trigger.super.remove();
    }
}
