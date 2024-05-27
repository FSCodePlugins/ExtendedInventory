package de.FSCode.ExtendedInventory.Utilities;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

@Getter
public class UpdateChecker {

    private final IMainframe<JavaPlugin> plugin;
    private final int resourceId = 116777;

    public UpdateChecker(IMainframe<JavaPlugin> plugin) {
        this.plugin = plugin;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin().getPluginInstance(), () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + getResourceId() + "/~").openStream();
                 Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogging().log("Unable to check for updates: " + e.getMessage());
            }
        });
    }
}