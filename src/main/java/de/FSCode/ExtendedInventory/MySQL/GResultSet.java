package de.FSCode.ExtendedInventory.MySQL;

import lombok.Getter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class GResultSet {

    private final PreparedStatement ps;
    private final ResultSet rs;
    private final GCallback<String> callback;

    public GResultSet(PreparedStatement ps, GCallback<String> callback) throws SQLException {
        this.ps = ps;
        this.rs = ps.executeQuery();
        this.callback = callback;
    }

    public void resolve(String toResolve) throws SQLException, IOException {
        if(!getRs().next()) {
            getCallback().resume(null);
            return;
        }
        getCallback().resume(getRs().getString(toResolve));
        getRs().close();
    }

}
