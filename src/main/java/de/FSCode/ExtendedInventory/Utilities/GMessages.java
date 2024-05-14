package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import lombok.Getter;

@Getter
public enum GMessages {

    ALREADY_FIRST_PAGE("Inventory.FirstPage"),
    NO_MORE_PAGES("Inventory.NoMorePages"),
    CONSOLE_SENDER("NoPlayer"),
    NO_PERMISSION("NoPermission");

    @Getter private static IMainframe plugin;

    private final String path;
    private String message;

    GMessages(String path) {
        this.path = path;
    }

    private boolean load(SpigotFileConfiguration fileConfiguration) {
        this.message = getPlugin().parseColors(fileConfiguration.getString(getPath()));
        return this.message != null;
    }

    public static boolean loadMessages(SpigotFileConfiguration fileConfiguration) {
        GMessages.plugin = fileConfiguration.getPlugin();
        for(GMessages messages : GMessages.values()) {
            if(!messages.load(fileConfiguration)) return false;
        }
        return true;
    }

}
