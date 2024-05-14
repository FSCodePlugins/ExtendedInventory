package de.FSCode.ExtendedInventory.MySQL;

import java.io.IOException;
import java.sql.SQLException;

public interface GCallback<T> {

    void resume(T result) throws SQLException, IOException;

}
