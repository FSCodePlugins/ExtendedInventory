package de.FSCode.ExtendedInventory.Utilities.FileHandling;

import de.FSCode.ExtendedInventory.Utilities.Exceptions.PathDoesNotExistException;

import java.util.Collection;
import java.util.List;

public interface IGFileConfiguration<E> {

    public String getString(String path);

    public Collection<String> getSection(String path);

    public Boolean getBoolean(String path);

    public Integer getInt(String path);

    public Long getLong(String path);

    public Object get(String path);

    public List<String> getStringList(String path);

    public E validate(String path) throws PathDoesNotExistException;

    public boolean isLoaded();

}
