package de.FSCode.ExtendedInventory.Utilities;

import de.FSCode.ExtendedInventory.Utilities.Exceptions.ItemGenerationException;
import de.FSCode.ExtendedInventory.Utilities.FileHandling.SpigotFileConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Getter
public enum GItems {

    PREVIOUS_PAGE("InventoryConfiguration.PreviousPageItem"),
    NEXT_PAGE("InventoryConfiguration.NextPageItem"),
    INDICATOR("InventoryConfiguration.IndicatorItem");

    @Getter private static IMainframe plugin;

    private final String path;

    private String name;
    private List<String> lore;
    private Material material;
    @Setter private String skinURL;
    @Setter private ItemStack item;

    GItems(String path) {
        this.path = path;
    }

    private ItemStack generateItemStack() throws ItemGenerationException {
        if(getMaterial() == null) throw new ItemGenerationException(getPlugin(), this, "Invalid material type");
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) throw new ItemGenerationException(getPlugin(), this, "ItemMeta could not be created");
        itemMeta.setDisplayName(getName());
        itemMeta.setLore(getLore());
        itemMeta.setLocalizedName(getLocalizedName());
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return hasCustomSkin() ? applySkin(item) : item;
    }

    private ItemStack applySkin(ItemStack item) throws ItemGenerationException {
        if(!item.getType().equals(Material.PLAYER_HEAD)) {
            ItemMeta tempMeta = item.getItemMeta();
            ItemStack updatedItem = new ItemStack(Material.PLAYER_HEAD);
            updatedItem.setItemMeta(tempMeta);
            item = updatedItem;
        }
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL("http://textures.minecraft.net/texture/" + getSkinURL()));
        } catch (MalformedURLException e) {
            getPlugin().getLogging().log(e);
            throw new ItemGenerationException(getPlugin(), this, "Error fetching custom skin");
        }
        profile.setTextures(textures);
        if(skullMeta == null) throw new ItemGenerationException(getPlugin(), this, "SkullMeta not found");
        skullMeta.setOwnerProfile(profile);
        item.setItemMeta(skullMeta);
        return item;
    }

    private void setMaterial(String material) {
        this.material = material == null ? null : Material.getMaterial(material.toUpperCase());
    }

    private void setName(String name) {
        this.name = name == null || name.isEmpty() ? " " : name;
    }

    private void setLore(String lore) {
        this.lore = (lore == null || lore.isEmpty()) ? null : List.of(lore.split("//"));
    }

    private boolean hasCustomSkin() {
        return getSkinURL() != null && getSkinURL().length() > 10;
    }

    public String getLocalizedName() {
        return "GITEMS_" + name();
    }

    public ItemStack getItem(String[] toReplace, String[] replacements) {
        ItemMeta itemMeta = getItem().getItemMeta();
        assert itemMeta != null;
        for(int i = 0; i < toReplace.length; i++) {
            itemMeta.setDisplayName(itemMeta.getDisplayName().replace(toReplace[i], replacements[i]));
            if(itemMeta.getLore() != null && !itemMeta.getLore().isEmpty()) {
                int tempFinal_i = i;
                itemMeta.setLore(itemMeta.getLore().stream().map(l -> l.replace(toReplace[tempFinal_i], replacements[tempFinal_i])).toList());
            }
        }
        ItemStack newItem = new ItemStack(getItem().getType());
        newItem.setItemMeta(itemMeta);
        return newItem;
    }

    public ItemStack getItem(String toReplace, String replacement) {
        return getItem(new String[] {toReplace}, new String[] {replacement});
    }

    private boolean load(SpigotFileConfiguration cfg) {
        setName(getPlugin().parseColors(cfg.getString(getPath() + ".Name")));
        setLore(getPlugin().parseColors(cfg.getString(getPath() + ".Lore")));
        setMaterial(cfg.getString(getPath() + ".Material"));
        setSkinURL(cfg.getString(getPath() + ".MinecraftURL"));
        try {
            setItem(generateItemStack());
        } catch (ItemGenerationException e) {
            getPlugin().getLogging().log(e);
            return false;
        }
        return getItem() != null;
    }

    public static boolean loadItems(SpigotFileConfiguration fileConfiguration) {
        GItems.plugin = fileConfiguration.getPlugin();
        for(GItems item : GItems.values()) {
            if(!item.load(fileConfiguration)) {return false;}
        }
        return true;
    }

}
