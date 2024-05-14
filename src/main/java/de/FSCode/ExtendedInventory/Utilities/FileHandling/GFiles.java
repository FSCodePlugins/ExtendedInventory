package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
public enum GFiles {

    GENERIC_DATA("/data", "metadata"),
    LOGS("/logs", "logs.txt"),
    MYSQL("", "mysql.yml"),
    CONFIG("","config.yml"),
    MESSAGES("","messages.yml");

    @Setter private String name;
    @Setter private File file;
    private String path;

    GFiles(String path, String name) {
        this.path = path;
        this.name = name;
    }

    private void load(String pluginFolder) {
        this.path = pluginFolder + this.path;
        setFile(new File(this.path, this.name));
    }

    public static void loadAllFiles(String pluginFolder) {
        for(GFiles file : values()) file.load(pluginFolder);
    }

}
