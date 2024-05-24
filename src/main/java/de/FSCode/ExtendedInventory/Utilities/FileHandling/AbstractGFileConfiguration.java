package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.Utilities.Exceptions.FileGenerationException;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Getter
public abstract class AbstractGFileConfiguration<E> implements IGFileConfiguration<E> {

    private final GFiles gFile;
    private final IMainframe<JavaPlugin> plugin;
    @Setter private E configuration;

    public AbstractGFileConfiguration(GFiles gFile, IMainframe<JavaPlugin> plugin) {
        this.gFile = gFile;
        this.plugin = plugin;
        if(gFile.getFile() == null) GFiles.loadAllFiles(plugin.getPluginFolder());
    }

    abstract InputStream getResource();

    public void saveResource() throws FileGenerationException {
        if (getGFile().getFile() == null || !getGFile().getFile().exists()) {
            try {
                new File(getGFile().getPath()).mkdirs();
                Files.copy(getPlugin().getResource(getGFile().getName()), getGFile().getFile().toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                getPlugin().getLogging().log(e);
                throw new FileGenerationException(plugin, gFile);
            }
        }
    }

}
