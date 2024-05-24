package de.FSCode.ExtendedInventory.Utilities.Exceptions;

import de.FSCode.ExtendedInventory.Utilities.FileHandling.GFiles;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;

public class FileGenerationException extends Exception {

    public FileGenerationException(IMainframe<?> plugin, GFiles file) {
        plugin.sendConsoleMessage("%PREFIX% &cCould not generate file " + file.getName());
    }

}
