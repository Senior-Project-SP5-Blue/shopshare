package com.sp5blue.shopshare;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvalidateOldTokens implements Trigger {
    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        Trigger.super.init(connection, s, s1, s2, b, i);
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        if (newRow[2] == "ACCESS") {
            try (PreparedStatement stmt = connection.prepareStatement("""
            UPDATE tokens
            SET is_revoked = true, is_expired = true
            WHERE tokens.shopper_id = (?)
            AND is_revoked = false
            AND tokens.type = 'ACCESS'""")) {
                stmt.setObject(1, newRow[3]);
            }
        } else if (newRow[2] == "REFRESH") {
            try (PreparedStatement stmt = connection.prepareStatement("""
            UPDATE tokens
            SET is_revoked = true, is_expired = true
            WHERE tokens.shopper_id = (?)
            AND tokens.type = 'REFRESH'""")) {
                stmt.setObject(1, newRow[3]);
            }
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
