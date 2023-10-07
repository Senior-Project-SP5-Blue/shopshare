package com.sp5blue.shopshare;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.SQLException;

public class ListCreateTrigger implements Trigger {
    @Override
    public void init(Connection connection, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
        Trigger.super.init(connection, schemaName, triggerName, tableName, before, type);
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        if (newRow[4] != null && newRow[4] != "") {
            newRow[3] = newRow[4];
        } else if (newRow[5] != null && newRow[5] != "") {
            newRow[3] = newRow[5];
        }
        else {
            throw new SQLException("Both shopper id and group id are null");
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
