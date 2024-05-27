package de.FSCode.ExtendedInventory;

import de.FSCode.ExtendedInventory.Commands.ReloadCommand;
import de.FSCode.ExtendedInventory.Listeners.GListeners;
import de.FSCode.ExtendedInventory.MySQL.ConnectionManager;
import de.FSCode.ExtendedInventory.MySQL.SQLInventoryManager;
import de.FSCode.ExtendedInventory.Utilities.*;
import de.FSCode.ExtendedInventory.Utilities.Exceptions.SQLInitializeException;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.FlatFileInventoryManager;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.GFiles;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.GLogging;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ExtendedInventory extends JavaPlugin implements IMainframe<JavaPlugin> {

    private String prefix = "&7[&6EInv&7] |";

    private boolean outdated = false;

    private SpigotFileConfiguration configuration, messageConfiguration, mysqlConfiguration;
    private GLogging logging;

    private ConnectionManager connectionManager;
    private AbstractGInventoryHandler inventoryHandler;

    @Override
    public void onEnable() {
        if(setupConfigurations()) {
            try {
                if(!getMysqlConfiguration().getBoolean("enable") || (getMysqlConfiguration().getBoolean("enable")
                                && ((this.connectionManager = new ConnectionManager(this, getMysqlConfiguration())).isInitialized()))) {

                    this.inventoryHandler = getConnectionManager() != null ? new SQLInventoryManager(this) : new FlatFileInventoryManager(this);
                    GListeners.initializeListeners(this);
                    getCommand("einv").setExecutor(new ReloadCommand(this));
                    this.prefix = GConfigs.PREFIX.getAsString(true);
                    sendConsoleMessage("%PREFIX% &aPlugin was loaded successfully! " + (getConnectionManager() != null ? "&a[&eMySQL connected&a]" : ""));
                    if(GConfigs.CHECK_UPDATES.getAsBoolean())
                        new UpdateChecker(this).getVersion(newestVersion -> {
                            if(!getDescription().getVersion().equals(newestVersion)) {
                                outdated = true;
                                sendConsoleMessage("%PREFIX% &6You are running an outdated version of ExtendedInventory!");
                                sendConsoleMessage("%PREFIX% &6Please go to spigotmc.org and update to the latest version!");
                            }
                        });
                    return;

                }
            } catch (SQLInitializeException ignored) {}
        }
        sendConsoleMessage("%PREFIX%");
        sendConsoleMessage("%PREFIX% &cThere were fatal errors during plugin initialization.");
        sendConsoleMessage("%PREFIX% &cThe plugin is shutting down..");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public boolean setupConfigurations() {
        this.logging = new GLogging(this);
        this.configuration = new SpigotFileConfiguration(this, GFiles.CONFIG);
        this.prefix = getConfiguration().getString("Prefix");
        this.messageConfiguration = new SpigotFileConfiguration(this, GFiles.MESSAGES);
        this.mysqlConfiguration = new SpigotFileConfiguration(this, GFiles.MYSQL);
        return getConfiguration().isLoaded()
                && getMessageConfiguration().isLoaded()
                && getMysqlConfiguration().isLoaded()
                && getLogging().isInitialized()
                && GConfigs.loadConfigs(this.configuration)
                && GMessages.loadMessages(this.messageConfiguration)
                && GItems.loadItems(this.configuration);
    }

    @Override
    public void onDisable() {
        for(Player players : Bukkit.getOnlinePlayers()) {
            try {
                getInventoryHandler().save(players, false);
            } catch (Exception e) {
                getLogging().log(e);
                sendConsoleMessage("%PREFIX% &cUnable to save data for player " + players.getName());
            }
        }
        if(getConnectionManager() != null) getConnectionManager().closePool();
    }

    @Override
    public void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(parseColors(message));
    }

    @Override
    public String parseColors(String text) {
        return text == null ? null : ChatColor.translateAlternateColorCodes('&', text.replace("%PREFIX%", getPrefix() + "&r"));
    }

    @Override
    public boolean reload() {
        return setupConfigurations();
    }

    @Override
    public String getPluginFolder() {
        return getDataFolder().getAbsolutePath();
    }

    @Override
    public GLogging getLogging() {
        return this.logging;
    }

    @Override
    public JavaPlugin getPluginInstance() {
        return this;
    }

    @Override
    public ConnectionManager getSQL() {
        return this.connectionManager;
    }

    @Override
    public boolean isOutdated() {return this.outdated;}

}
