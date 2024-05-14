package de.FSCode.ExtendedInventory.Utilities.Exceptions;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;

public class SQLInitializeException extends Exception {

    public SQLInitializeException(IMainframe plugin, String reason) {
        plugin.sendConsoleMessage("%PREFIX% &cThere was a problem initializing MySQL!");
        plugin.sendConsoleMessage("%PREFIX% &cReason: " + reason);
    }

}
