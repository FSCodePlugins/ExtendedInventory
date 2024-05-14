package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.GItems;
import de.FSCode.ExtendedInventory.Utilities.GMessages;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener extends AbstractGListener {

    public ItemListener(IMainframe plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(validateItem(e.getCurrentItem()) || validateItem(e.getCursor()) || validateItem(p.getItemOnCursor()) || validateItem(p.getItemInUse())) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);

            if(!validateItem(e.getCurrentItem())) return;
            if(e.getCurrentItem().getItemMeta().getLocalizedName().equals(GItems.PREVIOUS_PAGE.getLocalizedName())) {
                if(getPlugin().getInventoryHandler().getCurrentPage(p.getUniqueId()) == 0) {
                    p.sendMessage(GMessages.ALREADY_FIRST_PAGE.getMessage());
                    return;
                }
                try {
                    getPlugin().getInventoryHandler().switchPage(p, getPlugin().getInventoryHandler().getCurrentPage(p.getUniqueId())-1);
                } catch (Exception ex) {
                    getPlugin().getLogging().log(ex);
                    getPlugin().sendConsoleMessage("%PREFIX% &cThere was an error switching pages for player " + p.getName());
                }
                return;
            }
            if(e.getCurrentItem().getItemMeta().getLocalizedName().equals(GItems.NEXT_PAGE.getLocalizedName())) {
                if(getPlugin().getInventoryHandler().getCurrentPage(p.getUniqueId()) < (getPlugin().getInventoryHandler().getAllowedPages(p.getUniqueId())-1)) {
                    try {
                        getPlugin().getInventoryHandler().switchPage(p, getPlugin().getInventoryHandler().getCurrentPage(p.getUniqueId())+1);
                    } catch (Exception ex) {
                        getPlugin().getLogging().log(ex);
                        getPlugin().sendConsoleMessage("%PREFIX% &cThere was an error switching pages for player " + p.getName());
                    }
                    return;
                }
                p.sendMessage(GMessages.NO_MORE_PAGES.getMessage());
            }
        }
    }

    private boolean validateItem(ItemStack item) {
        if(item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()) {
            return item.getItemMeta().getLocalizedName().startsWith("GITEMS");
        }
        return false;
    }

}
