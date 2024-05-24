package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public enum GConfigs {

    INCLUDE_HOTBAR("IncludeHotbarItems"),
    PREFIX("Prefix");

    @Getter private static IMainframe<JavaPlugin> plugin;

    private final String path;
    private Object value;

    GConfigs(String path) {
        this.path = path;
    }

    private String getAsString() {return String.valueOf(this.value);}

    public Boolean getAsBoolean() {
        return Boolean.valueOf(getAsString());
    }

    public String getAsString(boolean parseColors) {
        return parseColors ? getPlugin().parseColors(getAsString()) : getAsString();
    }

    private boolean load(SpigotFileConfiguration fileConfiguration) {
        this.value = fileConfiguration.get(this.path);
        return value != null;
    }

    public static boolean loadConfigs(SpigotFileConfiguration fileConfiguration) {
        GConfigs.plugin = fileConfiguration.getPlugin();
        for(GConfigs configs : values()) if(!configs.load(fileConfiguration)) return false;
        return true;
    }

}
