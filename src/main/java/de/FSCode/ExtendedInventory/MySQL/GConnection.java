package de.FSCode.ExtendedInventory.MySQL;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class GConnection {

    private final Connection connection;

    public GConnection(Connection connection) {
        this.connection = connection;
    }

    public void executeUpdate(String sql) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        finish();
    }

    public GResultSet fetch(String sql, GCallback<String> callback) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(sql);
        return new GResultSet(ps, result -> {
            callback.resume(result);
            ps.close();
            finish();
        });
    }

    public void finish() throws SQLException {
        getConnection().close();
    }

}
