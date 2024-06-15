package pouce.gui;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pouce.HavaPouce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pouce.HavaPouce.getPlugin;

public class HavaGuiItem {
    private static Map<String, HavaGuiItem> itemMap = new HashMap<>();
    private NamespacedKey key;
    private ItemStack itemStack;
    private String uniqueIdValue;
    private String guiTarget;

    public HavaGuiItem(ItemStack itemStack, NamespacedKey key, String uniqueIdValue, String guiTarget) {
        this.key = key;
        this.itemStack = itemStack;
        this.uniqueIdValue = uniqueIdValue;
        this.guiTarget = guiTarget;
        itemMap.put(uniqueIdValue, this);
        saveToConfig();
    }

    public NamespacedKey getKey() {
        return key;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getUniqueIdValue() {
        return uniqueIdValue;
    }

    public String getGuiTarget() {
        return guiTarget;
    }

    public static List<String> getAllGuitem() {
        return new ArrayList<>(itemMap.keySet());
    }

    public void saveToConfig() {
        File file = new File(HavaPouce.getPlugin().getDataFolder(), "items.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String path = "items." + uniqueIdValue + ".";
        config.set(path + "itemStack", itemStack);
        config.set(path + "guiTarget", guiTarget);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteToConfig() {
        itemMap.remove(uniqueIdValue);

        File file = new File(HavaPouce.getPlugin().getDataFolder(), "items.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String path = "items." + uniqueIdValue;

        config.set(path, null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(ItemStack newItemStack, String newGuiTarget) {
        this.itemStack = newItemStack;
        this.guiTarget = newGuiTarget;
        saveToConfig();
    }

    public static void loadItemsFromConfig() {
        File file = new File(HavaPouce.getPlugin().getDataFolder(), "items.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.contains("items")) {
            ConfigurationSection itemSection = config.getConfigurationSection("items");
            for (String key : itemSection.getKeys(false)) {
                ItemStack itemStack = itemSection.getItemStack(key + ".itemStack");
                String guiTarget = itemSection.getString(key + ".guiTarget");
                new HavaGuiItem(itemStack, new NamespacedKey(HavaPouce.getPlugin(), "unique_id"), key, guiTarget);
            }
        }
    }

    public static HavaGuiItem getItemByUniqueIdValue(String uniqueIdValue) {
        return itemMap.get(uniqueIdValue);
    }

    public static ItemStack getItemByID(String uniqueIdValue){
        HavaGuiItem itemGui = itemMap.get(uniqueIdValue);
        ItemStack item = itemGui.getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemGui.getUniqueIdValue());
        NamespacedKey key = new NamespacedKey(getPlugin(), "unique_id");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, itemGui.getUniqueIdValue());
        item.setItemMeta(itemMeta);
        return item;
    }
}
