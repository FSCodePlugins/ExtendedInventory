package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.sql.SQLException;

public class JoinListener extends AbstractGListener {

    public JoinListener(IMainframe plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) throws SQLException, IOException {
        Player p = e.getPlayer();
        getPlugin().getInventoryHandler().load(p);
        getPlugin().getInventoryHandler().setControlItems(p);
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin().getJavaPlugin(), () -> {
            for(int i = 0; i <= 10; i++) {
                if(p.isOnline() && p.hasPermission("extendedinventory.pages." + i))
                    getPlugin().getInventoryHandler().setAllowedPages(p.getUniqueId(), i);
            }
        });
    }

}
