package de.FSCode.ExtendedInventory.Utilities.Exceptions;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;

public class PathDoesNotExistException extends Exception {

    public PathDoesNotExistException(IMainframe<?> plugin, String path, String file) {
        plugin.sendConsoleMessage("%PREFIX% &cCould not find path [" + path + "] in file " + file);
    }

}
