package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.sql.SQLException;

public class QuitListener extends AbstractGListener {

    public QuitListener(IMainframe plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) throws SQLException, IOException {
        Player p = e.getPlayer();
        getPlugin().getInventoryHandler().save(p, true);
    }

}
