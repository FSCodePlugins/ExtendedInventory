package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.MySQL.GCallback;
import de.FSCode.ExtendedInventory.Utilities.AbstractGInventoryHandler;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class FlatFileInventoryManager extends AbstractGInventoryHandler {

    private final HashMap<UUID, SpigotFileConfiguration> cachedConfigurations = new HashMap<>();

    public FlatFileInventoryManager(IMainframe<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public void updateDataForPageSynced(Player player, int page, GCallback<ItemStack[]> callback) {
        try {
            SpigotFileConfiguration configuration = getCachedConfigurations().getOrDefault(player.getUniqueId(), null);
            if(configuration == null) {
                configuration = new SpigotFileConfiguration(getPlugin(), GFiles.GENERIC_DATA);
                UTF8YamlConfiguration newConfig = new UTF8YamlConfiguration(new File(GFiles.GENERIC_DATA.getPath(), player.getUniqueId().toString() + ".data"), getPlugin());
                newConfig.load();
                configuration.setConfiguration(newConfig);
                getCachedConfigurations().put(player.getUniqueId(), configuration);
            }
            if(configuration.getConfiguration().getString(String.valueOf(page)) != null) {
                ItemStack[] updatedItems = getSerializer().deserialize(configuration.getString(String.valueOf(page)));
                setCachedContentsToPage(player.getUniqueId(), page, updatedItems);
                if(getCachedPages().getOrDefault(player.getUniqueId(), 0) == page) player.getInventory().setContents(updatedItems);
                callback.resume(updatedItems);
                return;
            }
            callback.resume(new ItemStack[] {});
        } catch (Exception ex) {
            getPlugin().getLogging().log(ex);
        }
    }

    @Override
    public void saveDataForPageSynced(Player player, int page) {
        SpigotFileConfiguration configuration = getCachedConfigurations().getOrDefault(player.getUniqueId(), null);
        if(configuration == null) {
            getPlugin().sendConsoleMessage("%PREFIX% &cUnable to save inventory data for player " + player.getName());
            return;
        }
        try {
            getContentsForPage(player, page, toSave -> {
                configuration.getConfiguration().set(String.valueOf(page), getSerializer().serialize(toSave));
                configuration.getConfiguration().save(new File(GFiles.GENERIC_DATA.getPath(), player.getUniqueId().toString() + ".data"));
            });
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
    }
}
