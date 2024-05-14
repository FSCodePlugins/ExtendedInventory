package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryDropListener extends AbstractGListener {

    public InventoryDropListener(IMainframe plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDeathEvent e) {
        Player p = e.getEntity();
        List<ItemStack> drops = removeControlItems(e.getDrops());
        e.getDrops().clear();
        e.getDrops().addAll(drops);
    }

    private List<ItemStack> removeControlItems(List<ItemStack> items) {
        return items.stream().filter(i -> !(i.hasItemMeta() && i.getItemMeta().hasLocalizedName()
                && i.getItemMeta().getLocalizedName().startsWith("GITEMS"))).toList();
    }

}
