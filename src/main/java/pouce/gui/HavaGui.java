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

public class HavaGui implements Listener {

    private static Map<String, HavaGui> guiMap = new HashMap<>();

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

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        for (HavaGui gui : guiMap.values()) {
            if (gui.guiContent.equals(closedInventory) && !player.hasMetadata("GuiProtect")) {
                gui.saveToConfig();
                break;
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(item != null){
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    NamespacedKey key = new NamespacedKey(getPlugin(), "unique_id");
                    String uniqueId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                    if (uniqueId != null) {
                        // L'UUID a été récupéré avec succès

                        if(isDebugMod()){
                            sendHavaMessage(player, "Unique ID: " + uniqueId);
                        }
                        try {
                            if(uniqueId.equals(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getUniqueIdValue()) && player.hasMetadata("GuiProtect")){
                                if(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getGuiTarget() != null){
                                    getGuiByName(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getGuiTarget()).openReadonlyInventory(player);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("[HavaPouce] : Erreur a corriger plus tard : " + e.getMessage());
                        }

                    }
                }
            }
            if(item.getType() == Material.RED_BED){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§7 BED WARS")){
                    Inventory inv = Bukkit.createInventory(null, 9, "combat");

                    ItemStack woodSword = new ItemStack(Material.WOODEN_SWORD);
                    ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
                    ItemMeta woodMeta = woodSword.getItemMeta();
                    woodMeta.addEnchant(Enchantment.DAMAGE_ALL, 250, true);
                    ItemMeta stoneMeta = stoneSword.getItemMeta();
                    woodSword.setItemMeta(woodMeta);
                    stoneSword.setItemMeta(stoneMeta);

                    inv.setItem(0, woodSword);
                    inv.setItem(2, woodSword);
                    player.openInventory(inv);


                }
            }
        }
    }
}
