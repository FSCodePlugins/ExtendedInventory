package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.GPermissions;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public class JoinListener extends AbstractGListener {

    public JoinListener(IMainframe<JavaPlugin> plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) throws SQLException, IOException {
        Player p = e.getPlayer();
        getPlugin().getInventoryHandler().load(p);
        getPlugin().getInventoryHandler().setControlItems(p);
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin().getPluginInstance(), () -> {
            for(int i = 0; i <= 10; i++) {
                getPlugin().getInventoryHandler().setAllowedPages(p.getUniqueId(), 0);
                if(GPermissions.INV_PAGES.check(p, String.valueOf(i)))
                    getPlugin().getInventoryHandler().setAllowedPages(p.getUniqueId(), i);
            }
        });

        if(getPlugin().isOutdated() && GPermissions.UPDATE_CHECK.check(p)) {
            p.sendMessage(getPlugin().getPrefix() + " §7There is a new update available!");
        }
    }

}
