package de.FSCode.ExtendedInventory.Listeners;

import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum GListeners {

    DROP_LISTENER(InventoryDropListener.class),
    ITEM_LISTENERS(ItemListener.class),
    QUIT_LISTENER(QuitListener.class),
    INVENTORY_LISTENERS(InventoryListeners.class),
    JOIN_LISTENER(JoinListener.class);

    private final Class<? extends AbstractGListener> listenerClass;

    GListeners(Class<? extends AbstractGListener> listener) {
        this.listenerClass = listener;
    }

    private void initialize(IMainframe<JavaPlugin> plugin) {
        try {
            getListenerClass().getConstructor(IMainframe.class).newInstance(plugin);
        } catch (Exception ex) {
            plugin.getLogging().log(ex);
        }
    }

    public static void initializeListeners(IMainframe<JavaPlugin> plugin) {
        for(GListeners listener : GListeners.values()) listener.initialize(plugin);
    }

}
