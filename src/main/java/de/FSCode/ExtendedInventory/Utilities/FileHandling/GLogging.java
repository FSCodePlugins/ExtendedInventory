package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class GLogging {

    private final SpigotFileConfiguration logConfiguration;
    private final IMainframe plugin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GLogging(IMainframe plugin) {
        this.plugin = plugin;
        this.logConfiguration = new SpigotFileConfiguration(plugin, GFiles.LOGS);
    }

    private void log(String prefix, String message) {
        try {
            getLogConfiguration().getConfiguration().set(getDateFormat().format(new Date(System.currentTimeMillis())) + "[ID" + prefix + "]", message);
            getLogConfiguration().getConfiguration().save(GFiles.LOGS.getFile());
        } catch (IOException e) {
            getPlugin().sendConsoleMessage("%PREFIX% &cError while writing logs. Printing full stack trace in console:");
            e.printStackTrace();
        }
    }

    public void log(String message) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        log(timeMillis.substring(timeMillis.length()-3), message);
    }

    public void log(Exception e) {
        log(String.valueOf(-2), "---- Begin ----");
        log(String.valueOf(-1), e.getMessage());
        for(int i = 0; i < e.getStackTrace().length; i++) {
            log(String.valueOf(i), e.getStackTrace()[i].toString());
        }
        log(String.valueOf(e.getStackTrace().length), "---- End ----");
    }

    public boolean isInitialized() {
        return getLogConfiguration().isLoaded();
    }

}
