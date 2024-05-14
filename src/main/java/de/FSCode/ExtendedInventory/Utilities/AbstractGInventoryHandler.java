package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.MySQL.GCallback;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@Getter
public abstract class AbstractGInventoryHandler {

    private final HashMap<UUID, HashMap<Integer, ItemStack[]>> cachedInventory = new HashMap<>();
    private final HashMap<UUID, Integer> cachedPages = new HashMap<>();
    private final HashMap<UUID, Integer> allowedPages = new HashMap<>();

    protected final ItemStackSerialization serializer;

    private final IMainframe plugin;
    private final boolean includeHotbar;

    public AbstractGInventoryHandler(IMainframe plugin) {
        this.plugin = plugin;
        this.serializer = new ItemStackSerialization(plugin);
        this.includeHotbar = GConfigs.INCLUDE_HOTBAR.getAsBoolean();
    }

    public void setAllowedPages(UUID uuid, int page) {
        allowedPages.put(uuid, page);
    }

    public int getAllowedPages(UUID uuid) {
        return allowedPages.getOrDefault(uuid, 1);
    }

    public int getCurrentPage(UUID uuid) {
        return getCachedPages().getOrDefault(uuid, 0);
    }

    public void setCurrentPage(UUID uuid, int page) {
        getCachedPages().put(uuid, page);
    }

    public void load(Player player) throws SQLException, IOException {
        updateDataForPage(player, getCurrentPage(player.getUniqueId()), (result) -> {
            setControlItems(player);
        });
    }

    public void switchPage(Player player, int newPage) throws SQLException, IOException {
        setCachedContents(player.getUniqueId(), player.getInventory().getContents());
        setCurrentPage(player.getUniqueId(), newPage);
        getContents(player, newItems -> {
            if(player.isOnline()) {

                ItemStack[] hotbarItems = new ItemStack[9];
                if(!includeHotbar) {
                    for(int i = 0; i < 9; i++) {
                        hotbarItems[i] = player.getInventory().getItem(i);
                    }
                }

                player.getInventory().setContents(newItems);
                setControlItems(player);

                if(!includeHotbar) {
                    for(int i = 0; i < 9; i++) {
                        player.getInventory().setItem(i, hotbarItems[i]);
                    }
                }
            }
        });
    }

    public void updateDataForCurrentPage(Player player, GCallback<ItemStack[]> updatedItems) throws SQLException, IOException {
        updateDataForPage(player, getCurrentPage(player.getUniqueId()), updatedItems);
    }

    protected void setCachedContents(UUID uuid, ItemStack[] itemStacks) {
        setCachedContentsToPage(uuid, getCurrentPage(uuid), itemStacks);
    }

    protected void setCachedContentsToPage(UUID uuid, int page, ItemStack[] itemStacks) {
        HashMap<Integer, ItemStack[]> inventory = getCachedInventory().getOrDefault(uuid, new HashMap<>());
        inventory.put(page, itemStacks);
        getCachedInventory().put(uuid, inventory);
    }

    private ItemStack[] getCachedContents(UUID uuid, int page) {
        return getCachedInventory().getOrDefault(uuid, new HashMap<>()).getOrDefault(page, new ItemStack[] {});
    }

    protected void getContents(Player player, GCallback<ItemStack[]> callback) throws SQLException, IOException {
        getContentsForPage(player, getCurrentPage(player.getUniqueId()), callback);
    }

    protected void getContentsForPage(Player player, int page, GCallback<ItemStack[]> callback) throws SQLException, IOException {
        HashMap<Integer, ItemStack[]> inventory = getCachedInventory().getOrDefault(player.getUniqueId(), new HashMap<>());
        if(inventory.containsKey(page)) {
            callback.resume(inventory.get(page));
            return;
        }
        updateDataForPage(player, page, callback);
    }

    protected void executeAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin().getJavaPlugin(), runnable);
    }

    public void save(Player player, boolean async) throws SQLException, IOException {
        setCachedContents(player.getUniqueId(), player.getInventory().getContents());
        for(Integer page : getCachedInventory().getOrDefault(player.getUniqueId(), new HashMap<>()).keySet()){
            if(!isItemStackEmpty(getCachedContents(player.getUniqueId(), page))) {
                if(async) saveDataForPage(player, page);
                else saveDataForPageSynced(player, page);
            }
        }
    }

    private boolean isItemStackEmpty(ItemStack[] items) {
        for(ItemStack item : items) {
            if(item == null || item.getType().equals(Material.AIR)) continue;
            if(item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
                    && item.getItemMeta().getLocalizedName().startsWith("GITEMS")) continue;
            return false;
        }
        return true;
    }

    public void setControlItems(Player player) {
        player.getInventory().setItem(17, GItems.NEXT_PAGE.getItem());
        player.getInventory().setItem(35, GItems.PREVIOUS_PAGE.getItem());
        ItemStack indicatorItem = GItems.INDICATOR.getItem("%PAGE%", String.valueOf(getCurrentPage(player.getUniqueId())+1));
        indicatorItem.setAmount(getCurrentPage(player.getUniqueId())+1);
        player.getInventory().setItem(26, indicatorItem);
    }

    public void updateDataForPage(Player player, int page, GCallback<ItemStack[]> updatedItems) {
        executeAsync(() -> {
            try {
                updateDataForPageSynced(player, page, updatedItems);
            } catch (Exception e) {
                getPlugin().getLogging().log(e);
                getPlugin().sendConsoleMessage("%PREFIX% &cUnable to update data for page " + page + " of player " + player.getName());
            }
        });
    }

    public void saveDataForPage(Player player, int page) {
        executeAsync(() -> {
            try {
                saveDataForPageSynced(player, page);
            } catch (Exception e) {
                getPlugin().getLogging().log(e);
                getPlugin().sendConsoleMessage("%PREFIX% &cUnable to save data for player " + player.getName());
            }
        });
    }

    public abstract void updateDataForPageSynced(Player player, int page, GCallback<ItemStack[]> updatedItems) throws SQLException, IOException;

    public abstract void saveDataForPageSynced(Player player, int page) throws SQLException, IOException;

}
