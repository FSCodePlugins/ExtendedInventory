package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.Utilities.Exceptions.PathDoesNotExistException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IGFileConfiguration<E> {

    public String getString(String path);

    public String getStringForSetup(String path, String fallback);

    public Collection<String> getSection(String path);

    public Boolean getBoolean(String path);

    public Integer getInt(String path);

    public Long getLong(String path);

    public Object getObject(String path);

    public Object getObjectForSetup(String path, String fallback);

    public List<String> getStringList(String path);

    public E validate(String path, String fallback) throws PathDoesNotExistException, IOException;

    public boolean isLoaded();

}
