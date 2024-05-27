package de.FSCode.ExtendedInventory.Utilities;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public enum GPermissions {

    CMD_RELOAD("extendedinventory.commands.reload"),
    INV_PAGES("extendedinventory.pages.#"),
    UPDATE_CHECK("extendedinventory.updates");

    private final String permission;

    GPermissions(String permission) {
        this.permission = permission;
    }

    public boolean check(Player player, String replace) {
        return player.isOnline() && player.hasPermission(getPermission().replace("#", replace));
    }

    public boolean check(Player player) {
        return check(player, "");
    }

}
