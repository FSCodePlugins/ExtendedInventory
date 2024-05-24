package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AbstractGListener implements Listener {

    private final IMainframe<JavaPlugin> plugin;

    public AbstractGListener(IMainframe<JavaPlugin> plugin) {
        this.plugin = plugin;
        getPlugin().getPluginInstance().getServer().getPluginManager()
                .registerEvents(this, getPlugin().getPluginInstance());
    }

}
