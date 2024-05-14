package de.FSCode.ExtendedInventory.Utilities;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Getter
public class ItemStackSerialization {

    private final IMainframe plugin;

    public ItemStackSerialization(IMainframe plugin) {
        this.plugin = plugin;
    }

    public ItemStack[] deserialize(String data) {
        if(data == null || data.isEmpty()) return new ItemStack[] {};
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (ClassNotFoundException | IOException e) {
            getPlugin().getLogging().log(e);
            getPlugin().sendConsoleMessage("%PREFIX% &cUnable to deserialize inventory-data!");
            return new ItemStack[] {};
        }
    }

    public String serialize(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            getPlugin().getLogging().log(e);
            getPlugin().sendConsoleMessage("%PREFIX% &cCannot serialize inventory-data!");
            return null;
        }
    }

}
