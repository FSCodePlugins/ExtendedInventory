package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.MySQL.ConnectionManager;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.GLogging;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;

public interface IMainframe<T> {

    public void sendConsoleMessage(String message);

    public InputStream getResource(String path);

    public String parseColors(String text);

    public String getPluginFolder();

    public GLogging getLogging();

    public T getPluginInstance();

    public ConnectionManager getSQL();

    public AbstractGInventoryHandler getInventoryHandler();

    public boolean reload();

}
