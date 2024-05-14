package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class UTF8YamlConfiguration extends YamlConfiguration {

    private final File file;
    @Getter private final IMainframe plugin;

    public UTF8YamlConfiguration(File file, IMainframe plugin) {
        this.file = file;
        this.plugin = plugin;
    }

    public void save(File file) throws IOException {
        Files.createParentDirs(file);
        final String data = this.saveToString();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        }
    }

    public boolean loadConfig() {
        try {
            this.load(file);
            return true;
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
            getPlugin().sendConsoleMessage("%PREFIX% &c--------------------------------------------------");
            getPlugin().sendConsoleMessage("%PREFIX%");
            getPlugin().sendConsoleMessage("%PREFIX% &cIt seems like you made a major mistake editing your " + file.getName() + " :(");
            getPlugin().sendConsoleMessage("%PREFIX% &cThis error often idicates a missing ' (quotationmark) or : (colon) somewhere..");
            getPlugin().sendConsoleMessage("%PREFIX% &c");
            getPlugin().sendConsoleMessage("%PREFIX% &cIf you cannot find the error, you have to delete your " + file.getName() +
                    " entirely to gerneate a new one");
            getPlugin().sendConsoleMessage("%PREFIX% &cExtendedInventory will get disabled to avoid crashes.");
            getPlugin().sendConsoleMessage("%PREFIX%");
            getPlugin().sendConsoleMessage("%PREFIX% &c--------------------------------------------------");
            return false;
        }
    }

    public void load() throws IOException, InvalidConfigurationException {
        if (file.exists()) {
            this.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
        }
    }
}
