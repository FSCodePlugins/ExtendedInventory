package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum GConfigs {

    CHECK_UPDATES("CheckForUpdates", "true"),
    INCLUDE_HOTBAR("IncludeHotbarItems", "false"),
    PREFIX("Prefix", "&7[&6EInv&7] |");

    @Getter private static IMainframe<JavaPlugin> plugin;

    private final String fallback;
    private final String path;
    private Object value;

    GConfigs(String path, String fallback) {
        this.path = path;
        this.fallback = fallback;
    }

    private String getAsString() {return String.valueOf(this.value);}

    public Boolean getAsBoolean() {
        return Boolean.valueOf(getAsString());
    }

    public String getAsString(boolean parseColors) {
        return parseColors ? getPlugin().parseColors(getAsString()) : getAsString();
    }

    private boolean load(SpigotFileConfiguration fileConfiguration) {
        this.value = fileConfiguration.getObjectForSetup(getPath(), getFallback());
        return value != null;
    }

    public static boolean loadConfigs(SpigotFileConfiguration fileConfiguration) {
        GConfigs.plugin = fileConfiguration.getPlugin();
        for(GConfigs configs : values()) if(!configs.load(fileConfiguration)) return false;
        return true;
    }

}
