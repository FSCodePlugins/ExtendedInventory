package de.FSCode.ExtendedInventory.MySQL;

import de.FSCode.ExtendedInventory.Utilities.AbstractGInventoryHandler;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;

@Getter
public class SQLInventoryManager extends AbstractGInventoryHandler {

    private final ConnectionManager connectionManager;

    public SQLInventoryManager(IMainframe plugin) {
        super(plugin);
        this.connectionManager = plugin.getSQL();
    }

    @Override
    public void saveDataForPageSynced(Player player, int page) {
        try {
            getContentsForPage(player, page, (itemContents) -> {
                String serializedContents = getSerializer().serialize(itemContents);
                getConnectionManager().getConnection().executeUpdate("INSERT INTO einv_inventories(uuid, page, inventory) " +
                        "VALUES ('" + player.getUniqueId().toString() + "','" + page + "','" + serializedContents + "') " +
                        "ON DUPLICATE KEY UPDATE inventory='" + serializedContents + "'");
            });
        } catch (SQLException | IOException e) {
            getPlugin().getLogging().log(e);
            getPlugin().sendConsoleMessage("%PREFIX% &cUnable to save inventory data for player " + player.getName());
        }
    }

    @Override
    public void updateDataForPageSynced(Player player, int page, GCallback<ItemStack[]> updatedItems) {
        try {
            getConnectionManager().getConnection().fetch("SELECT inventory FROM einv_inventories WHERE uuid='"
                    + player.getUniqueId().toString() + "' AND page='" + page + "'", result -> {
                ItemStack[] updatedContents = getSerializer().deserialize(result);
                setCachedContentsToPage(player.getUniqueId(), page, updatedContents);
                if(getCachedPages().getOrDefault(player.getUniqueId(), 0) == page) player.getInventory().setContents(updatedContents);
                updatedItems.resume(updatedContents);
            }).resolve("inventory");
        } catch (SQLException | IOException e) {
            getPlugin().getLogging().log(e);
            getPlugin().sendConsoleMessage("%PREFIX% &cCould not load inventory for player " + player.getName() + "!");
        }
    }

}
