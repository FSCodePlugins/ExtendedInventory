package de.FSCode.ExtendedInventory.Utilities.Exceptions;

import de.FSCode.ExtendedInventory.Utilities.GItems;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;

public class ItemGenerationException extends Exception {

    public ItemGenerationException(IMainframe<?> plugin, GItems item, String message) {
        plugin.sendConsoleMessage("%PREFIX% &cCould not generate item " + item.getPath());
        plugin.sendConsoleMessage("%PREFIX% &cReason: " + message);
    }

}
