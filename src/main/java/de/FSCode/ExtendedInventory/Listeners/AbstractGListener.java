package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public class AbstractGListener implements Listener {

    private final IMainframe plugin;

    public AbstractGListener(IMainframe plugin) {
        this.plugin = plugin;
        getPlugin().getJavaPlugin().getServer().getPluginManager()
                .registerEvents(this, getPlugin().getJavaPlugin());
    }

}
