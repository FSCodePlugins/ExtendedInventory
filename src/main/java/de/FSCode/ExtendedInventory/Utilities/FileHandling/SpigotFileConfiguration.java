package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.Utilities.Exceptions.FileGenerationException;
import de.FSCode.ExtendedInventory.Utilities.Exceptions.PathDoesNotExistException;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Getter
public class SpigotFileConfiguration extends AbstractGFileConfiguration<FileConfiguration> {

    public SpigotFileConfiguration(IMainframe plugin, GFiles gFile) {
        super(gFile, plugin);
        try {
            saveResource();
            UTF8YamlConfiguration utf8YamlConfiguration = new UTF8YamlConfiguration(gFile.getFile(), getPlugin());
            if(utf8YamlConfiguration.loadConfig())
                setConfiguration(utf8YamlConfiguration);
        } catch (FileGenerationException e) {
            getPlugin().getLogging().log(e);
        }
    }

    @Override
    InputStream getResource() {
        return getPlugin().getResource(getGFile().getName());
    }

    @Override
    public String getString(String path) {
        try {
            return validate(path).getString(path);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Collection<String> getSection(String path) {
        try {
            return validate(path).getConfigurationSection(path).getKeys(false);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Boolean getBoolean(String path) {
        try {
            return validate(path).getBoolean(path);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Object get(String path) {
        try {
            return validate(path).get(path);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Integer getInt(String path) {
        try {
            return validate(path).getInt(path);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Long getLong(String path) {
        try {
            return validate(path).getLong(path);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public List<String> getStringList(String path) {
        try {
            return validate(path).getStringList(path);
        } catch (PathDoesNotExistException e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public FileConfiguration validate(String path) throws PathDoesNotExistException {
        if(!isLoaded() || getConfiguration().get(path) == null)
            throw new PathDoesNotExistException(getPlugin(), path, getGFile().getName());
        return getConfiguration();
    }

    @Override
    public boolean isLoaded() {
        return getConfiguration() != null;
    }

}
