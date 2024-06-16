package pouce.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import pouce.HavaPouce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pouce.HavaPouce.*;

public class HavaGui {

    private static Map<String, HavaGui> guiMap = new HashMap<>();
    public static Map<String, HavaGui> GetGuiMap() {
        return guiMap;
    }

    private String guiName;
    private int guiSize;
    private Inventory guiContent;

    public HavaGui() {
    }

    public HavaGui(String name, int size) {
        this.guiName = name;
        this.guiSize = size;
        this.guiContent = Bukkit.createInventory(null, size, name);
        guiMap.put(name, this); // Ajouter cette instance à la map
    }

    public String getGuiName() {
        return guiName;
    }
    public Inventory getGuiContent() {
        return guiContent;
    }


    public void openInventory(Player player) {
        player.openInventory(guiContent);
    }

    public void openReadonlyInventory(Player player) {

        Inventory inv = Bukkit.createInventory(null, guiSize, guiName);
        inv.setContents(guiContent.getContents());
        player.openInventory(inv);
        player.setMetadata("GuiProtect", new FixedMetadataValue(getPlugin(), "GuiProtect"));
    }

    public void saveToConfig() {
        File file = new File(HavaPouce.getPlugin().getDataFolder(), "gui.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Sauvegarde des propriétés de la GUI
        String path = "gui." + guiName + ".";
        config.set(path + "size", guiSize);

        // Sauvegarde des items de l'inventaire
        ConfigurationSection itemsSection = config.createSection(path + "items");
        for (int i = 0; i < guiContent.getSize(); i++) {
            ItemStack item = guiContent.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                itemsSection.set(String.valueOf(i), item);
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGuisFromConfig() {
        File file = new File(HavaPouce.getPlugin().getDataFolder(), "gui.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.contains("gui")) {
            ConfigurationSection guiSection = config.getConfigurationSection("gui");
            for (String key : guiSection.getKeys(false)) {
                String guiName = key;
                int size = guiSection.getInt(guiName + ".size");

                Inventory guiContent = Bukkit.createInventory(null, size, guiName);

                ConfigurationSection itemsSection = guiSection.getConfigurationSection(guiName + ".items");
                if (itemsSection != null) {
                    for (String slotStr : itemsSection.getKeys(false)) {
                        int slot = Integer.parseInt(slotStr);
                        ItemStack item = itemsSection.getItemStack(slotStr);
                        guiContent.setItem(slot, item);
                    }
                }

                HavaGui gui = new HavaGui(guiName, size);
                gui.guiContent = guiContent; // Réassigner l'inventaire chargé
                guiMap.put(guiName, gui); // Ajouter cette instance à la map
            }
        }

        // Charger les items
        HavaGuiItem.loadItemsFromConfig();
    }

    public void deleteGui() {
        guiMap.remove(guiName);

        File file = new File(HavaPouce.getPlugin().getDataFolder(), "gui.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("gui." + guiName, null); // Supprimer du gui.yml

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HavaGui getGuiByName(String name) {
        return guiMap.get(name);
    }

    public static List<String> getAllGuiNames() {
        return new ArrayList<>(guiMap.keySet());
    }
}
