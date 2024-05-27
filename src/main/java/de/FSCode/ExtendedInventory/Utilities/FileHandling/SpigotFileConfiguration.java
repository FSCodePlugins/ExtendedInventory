package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.Utilities.Exceptions.FileGenerationException;
import de.FSCode.ExtendedInventory.Utilities.Exceptions.PathDoesNotExistException;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Getter
public class SpigotFileConfiguration extends AbstractGFileConfiguration<FileConfiguration> {

    public SpigotFileConfiguration(IMainframe<JavaPlugin> plugin, GFiles gFile) {
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
        return getStringForSetup(path, null);
    }

    @Override
    public String getStringForSetup(String path, String fallback) {
        try {
            return validate(path, fallback).getString(path);
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Collection<String> getSection(String path) {
        try {
            return validate(path, null).getConfigurationSection(path).getKeys(false);
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Boolean getBoolean(String path) {
        try {
            return validate(path, null).getBoolean(path);
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Object getObject(String path) {
        return getObjectForSetup(path, null);
    }

    @Override
    public Object getObjectForSetup(String path, String fallback) {
        try {
            return validate(path, fallback).get(path);
        } catch (Exception ex) {
            getPlugin().getLogging().log(ex);
        }
        return null;
    }

    @Override
    public Integer getInt(String path) {
        try {
            return validate(path, null).getInt(path);
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public Long getLong(String path) {
        try {
            return validate(path, null).getLong(path);
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public List<String> getStringList(String path) {
        try {
            return validate(path, null).getStringList(path);
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
        }
        return null;
    }

    @Override
    public FileConfiguration validate(String path, String fallback) throws PathDoesNotExistException, IOException {
        if(!isLoaded() || getConfiguration().get(path) == null) {
            if(fallback != null) {
                getConfiguration().set(path, fallback);
                getConfiguration().save(getGFile().getFile());
                return validate(path, null);
            }
            throw new PathDoesNotExistException(getPlugin(), path, getGFile().getName());
        }
        return getConfiguration();
    }

    @Override
    public boolean isLoaded() {
        return getConfiguration() != null;
    }

}
