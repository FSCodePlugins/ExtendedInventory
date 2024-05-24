package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryListeners extends AbstractGListener {

    public InventoryListeners(IMainframe<JavaPlugin> plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(validateItem(e.getItem()) || validateItem(p.getItemInUse()) || validateItem(p.getItemOnCursor())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryPickupItem(InventoryPickupItemEvent e) {
        if(validateItem(e.getItem().getItemStack())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSwapHandItem(PlayerSwapHandItemsEvent e) {
        if(validateItem(e.getMainHandItem()) || validateItem(e.getOffHandItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryMoveItem(InventoryMoveItemEvent e) {
        if(validateItem(e.getItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickup(EntityPickupItemEvent e) {
        if(validateItem(e.getItem().getItemStack())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItem(InventoryInteractEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(validateItem(p.getItemOnCursor()) || validateItem(p.getItemInUse())) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    private boolean validateItem(ItemStack item) {
        if(item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()) {
            return item.getItemMeta().getLocalizedName().startsWith("GITEMS");
        }
        return false;
    }


}
