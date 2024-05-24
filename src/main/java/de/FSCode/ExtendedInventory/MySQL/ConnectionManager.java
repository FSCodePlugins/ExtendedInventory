package de.FSCode.ExtendedInventory.MySQL;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.FSCode.ExtendedInventory.Utilities.Exceptions.SQLInitializeException;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class ConnectionManager {

    private final IMainframe<JavaPlugin> plugin;
    private final String url;
    private final String user;
    private final String password;
    private final String driverClass;
    private final int minConnections;
    private final int maxConnections;
    private final long timeout;

    private HikariDataSource dataSource;

    public ConnectionManager(IMainframe<JavaPlugin> plugin, String url, String user, String password, String driverClass, int minConnections, int maxConnections, long timeout) throws SQLInitializeException {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driverClass = driverClass;
        this.plugin = plugin;
        this.minConnections = minConnections;
        this.maxConnections = maxConnections;
        this.timeout = timeout;
        initialize();
        try {
            setupTables();
        } catch (SQLException e) {
            getPlugin().getLogging().log(e);
            throw new SQLInitializeException(plugin, "Could not create database tables!");
        }
    }

    public ConnectionManager(IMainframe<JavaPlugin> plugin, SpigotFileConfiguration configuration) throws SQLInitializeException {
        this(plugin,
                configuration.getString("url"),
                configuration.getString("username"),
                configuration.getString("password"),
                configuration.getString("ExpertSettings.DriverClass"),
                configuration.getInt("ExpertSettings.MinimumConnections"),
                configuration.getInt("ExpertSettings.MaximumConnections"),
                configuration.getLong("ExpertSettings.ConnectionTimeout")
        );
    }

    private void initialize() throws SQLInitializeException {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(getUrl());
            config.setDriverClassName(getDriverClass());
            config.setUsername(getUser());
            config.setPassword(getPassword());
            config.setMaximumPoolSize(getMaxConnections());
            config.setMinimumIdle(getMinConnections());
            config.setConnectionTimeout(getTimeout());
            config.setConnectionTestQuery("show tables");
            dataSource = new HikariDataSource(config);
        } catch (Exception ex) {
            getPlugin().getLogging().log(ex);
            throw new SQLInitializeException(getPlugin(), "Could not connect to MySQL-Database!");
        }
    }

    private void setupTables() throws SQLException {
        GConnection conn = getConnection();
        conn.executeUpdate("CREATE TABLE IF NOT EXISTS einv_inventories(uuid VARCHAR(50) NOT NULL, page INT NOT NULL, inventory TEXT, PRIMARY KEY(uuid, page))");
        conn.finish();
    }

    public GConnection getConnection() throws SQLException {
        return new GConnection(getDataSource().getConnection());
    }

    private void close(Connection con, PreparedStatement ps, ResultSet rs) {
        if (con != null) try { con.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
    }

    public boolean isInitialized() {
        return getDataSource() != null;
    }

    public void closePool() {
        if(getDataSource() != null) getDataSource().close();
    }

}
