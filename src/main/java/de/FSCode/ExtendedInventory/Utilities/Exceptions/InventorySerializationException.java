package de.FSCode.ExtendedInventory.Utilities.Exceptions;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.entity.Player;

public class InventorySerializationException extends Exception {

    public InventorySerializationException(IMainframe plugin, String player, String reason) {
        plugin.sendConsoleMessage("%PREFIX% &cCould serialize inventory for player " + player);
        plugin.sendConsoleMessage("%PREFIX% &cReason: " + reason);
    }

}
