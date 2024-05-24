package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum GMessages {

    CMD_UNKNOWN("Commands.UnknownCommand", "%PREFIX% &cUnknown command!"),
    CMD_WRONG_USAGE("Commands.WrongUsage", "%PREFIX% &7Wrong usage! -> %USAGE%"),
    CMD_RELOAD("Commands.Reload", "%PREFIX% &ePlugin has been reloaded!"),
    ALREADY_FIRST_PAGE("Inventory.FirstPage", null),
    NO_MORE_PAGES("Inventory.NoMorePages", null),
    CONSOLE_SENDER("NoPlayer", null),
    NO_PERMISSION("NoPermission", null);

    @Getter private static IMainframe<JavaPlugin> plugin;

    private final String path;
    private String message;
    private String fallback;

    GMessages(String path, String fallbackMessage) {
        this.path = path;
        this.fallback = fallbackMessage;
    }

    private boolean load(SpigotFileConfiguration fileConfiguration) {
        this.message = getPlugin().parseColors(fileConfiguration.getStringForSetup(getPath(), getFallback()));
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
