package pouce.items.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pouce.HavaPouce;
import pouce.gui.HavaGui;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.HavaUtilitaireItems;
import pouce.items.rarity.HavaRarity;
import pouce.items.spells.HavaSpell;
import pouce.items.spells.utils.HavaSpellUtils;
import pouce.nbt.HavaNBT;


import java.io.File;
import java.io.IOException;
import java.util.*;

import static pouce.HavaPouce.getPlugin;

public class HavaItemsUtils {

    private static Map<String, HavaItems> itemsMap = new HashMap<>();

    private static File file;
    private static FileConfiguration customConfig;

    public static void loadItemsConfig(){
        file = new File(HavaPouce.getPlugin().getDataFolder(), "items.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customConfig = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection guiSection = customConfig.getConfigurationSection("items");
        for (String key : guiSection.getKeys(false)) {
            HavaItems item = null;
            String uniqueName = key;

            String path = "items." + uniqueName;

            HavaRarity rarity = HavaRarity.valueOf(customConfig.getString(path + ".rarity"));
            ItemStack itemStack = customConfig.getItemStack(path + ".item");
            String donjonType = customConfig.getString(path + ".donjonType");

            if ("melee".equalsIgnoreCase(donjonType)) {
                int damage = customConfig.getInt(path + ".damage");
                int strength = customConfig.getInt(path + ".strength");
                HavaSpell spell = HavaSpellUtils.getSpell(customConfig.getString(path + ".spell"));
                item = new HavaMeleeItems(uniqueName, rarity, itemStack, damage, strength, spell);
            } else if ("distance".equalsIgnoreCase(donjonType)) {
                int damage = customConfig.getInt(path + ".damage");
                int strength = customConfig.getInt(path + ".strength");
                item = new HavaDistanceItems(uniqueName, rarity, itemStack, damage, strength);
            } else if ("utilitaire".equalsIgnoreCase(donjonType)) {
                item = new HavaUtilitaireItems(uniqueName, rarity, itemStack);
            } else{
                System.out.println("\u001B[31m" + "[HavaPouce] " + uniqueName + "ITEM NON RECONNUE" + "\u001B[0m");
            }

            if(item != null){
                itemsMap.put(uniqueName, item);
            }

        }

    }

    public static boolean saveItem(HavaItems item) {
        ItemStack itemStack = GetItemStack(item);

        String path = "items." + item.getUniqueName();
        customConfig.set(path + ".rarity", item.getRarity().toString());
        customConfig.set(path + ".item", itemStack);

        if (item instanceof HavaMeleeItems) {
            HavaMeleeItems meleeItem = (HavaMeleeItems) item;
            customConfig.set(path + ".donjonType", "melee");
            customConfig.set(path + ".damage", meleeItem.getDamage());
            customConfig.set(path + ".strength", meleeItem.getStrength());
            customConfig.set(path + ".spell", meleeItem.getSpell().getUniqueName());
            save();
            return true;
        } else if (item instanceof HavaDistanceItems){
            HavaDistanceItems distanceItem = (HavaDistanceItems) item;
            customConfig.set(path + ".donjonType", "distance");
            customConfig.set(path + ".damage",  distanceItem.getDamage());
            customConfig.set(path + ".strength", distanceItem.getStrength());
            save();
            return true;
        }
        else if (item instanceof HavaUtilitaireItems) {
            customConfig.set(path + ".donjonType", "utilitaire");
            save();
            return true;
        }
        return false;
    }

    public static HavaItems loadItem(String uniqueName) {

        itemsMap.get(uniqueName);

        if (!itemsMap.containsKey(uniqueName)) {
            System.out.println("\u001B[31m" + "[HavaPouce] " + uniqueName + "ITEM NON RECONNUE" + "\u001B[0m");
        }

        return itemsMap.get(uniqueName);
    }

    public static void deleteItem(String uniqueName) {
        String path = "items." + uniqueName;
        if (!customConfig.contains(path)) {
            return;
        }

        customConfig.set(path, null);
        itemsMap.remove(uniqueName);
        save();
    }


    private static void save() {
        try {
            customConfig.save(file);
            loadItemsConfig();
            UpdateAllOnlinePlayerItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//  -----------------------------  GET

    public static ItemStack GetItemStack(HavaItems item){
        ItemStack itemStack = item.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

        List<String> itemLore = new ArrayList<>();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey keyItemType = new NamespacedKey(getPlugin(), HavaNBT.GetItemDonjonType());

        NamespacedKey keyID = new NamespacedKey(getPlugin(), HavaNBT.GetItemDonjonID());
        NamespacedKey keyDamage = new NamespacedKey(getPlugin(), HavaNBT.GetItemDamage());
        NamespacedKey keyStrength = new NamespacedKey(getPlugin(), HavaNBT.GetItemStrength());
        NamespacedKey keyRarity = new NamespacedKey(getPlugin(), HavaNBT.GetItemRarity());
        NamespacedKey keySpell = new NamespacedKey(getPlugin(), HavaNBT.GetItemSpell());

        container.set(keyID, PersistentDataType.STRING, item.getUniqueName());
        container.set(keyRarity, PersistentDataType.STRING, item.getRarity().toString());
        itemLore.add("§f -=§m============================§r§f=- ");

        itemLore.add("");
        itemLore.add("  " +  item.getRarity().getFormattedName() + " ");

        String mark = " §c>§6>§e> ";


        if(item instanceof HavaMeleeItems){
            HavaMeleeItems meleeItems = (HavaMeleeItems) item;
            itemLore.add("");
            itemLore.add(mark + "§7Dégat ➡§6 " + meleeItems.getDamage() + "\uD83E\uDE93");
            itemLore.add(mark + "§7Force ➡§c " + meleeItems.getStrength() + "\uD83D\uDCA5");
            container.set(keyItemType, PersistentDataType.STRING, meleeItems.getType());
            container.set(keyDamage, PersistentDataType.INTEGER, meleeItems.getDamage());
            container.set(keyStrength, PersistentDataType.INTEGER, meleeItems.getStrength());
            container.set(keySpell, PersistentDataType.STRING, meleeItems.getSpell().getUniqueName());
            itemLore.add("");
            itemLore.addAll(meleeItems.getSpell().getLore());
        }
        else if(item instanceof HavaDistanceItems){
            HavaDistanceItems distanceItems = (HavaDistanceItems) item;
            itemLore.add(mark + "§7Dégat ➡§6 " + distanceItems.getDamage() + "\uD83E\uDE93");
            itemLore.add(mark + "§7Force ➡§c " + distanceItems.getStrength() + "\uD83D\uDCA5");
            container.set(keyItemType, PersistentDataType.STRING, distanceItems.getType());
            container.set(keyDamage, PersistentDataType.INTEGER, distanceItems.getDamage());
            container.set(keyStrength, PersistentDataType.INTEGER, distanceItems.getStrength());
        }
        else if(item instanceof HavaUtilitaireItems){
            HavaUtilitaireItems utilitaireItems = (HavaUtilitaireItems) item;
            container.set(keyItemType, PersistentDataType.STRING, utilitaireItems.getType());
        }

        itemLore.add("");

        itemLore.add("§f -=§m============================§r§f=- ");

        itemMeta.setLore(itemLore);

        itemMeta.setUnbreakable(true);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static List<HavaMeleeItems> GetMeleeItems() {
        List<HavaMeleeItems> meleeItems = new ArrayList<>();

        for (Map.Entry<String, HavaItems> entry : itemsMap.entrySet()) {
            HavaItems item = entry.getValue();

            if(item instanceof HavaMeleeItems){
                meleeItems.add((HavaMeleeItems) item);
            }
        }

        return meleeItems;
    }
    public static List<HavaDistanceItems> GetDistanceItems() {
        List<HavaDistanceItems> distanceItems = new ArrayList<>();

        for (Map.Entry<String, HavaItems> entry : itemsMap.entrySet()) {
            HavaItems item = entry.getValue();

            if(item instanceof HavaDistanceItems){
                distanceItems.add((HavaDistanceItems) item);
            }
        }

        return distanceItems;
    }
    public static List<HavaUtilitaireItems> GetUtilitaireItems() {
        List<HavaUtilitaireItems> utilitaireItems = new ArrayList<>();

        for (Map.Entry<String, HavaItems> entry : itemsMap.entrySet()) {
            HavaItems item = entry.getValue();

            if(item instanceof HavaUtilitaireItems){
                utilitaireItems.add((HavaUtilitaireItems) item);
            }
        }

        return utilitaireItems;
    }


    // Update

    public static void UpdateAllOnlinePlayerItems() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Inventory inv = onlinePlayer.getInventory();

            for (ItemStack itemStack : inv.getContents()) {
                if (itemStack == null) continue;

                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta == null) continue;

                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                NamespacedKey keyID = new NamespacedKey(getPlugin(), HavaNBT.GetItemDonjonID());

                if (container.has(keyID, PersistentDataType.STRING)) {
                    String uniqueName = container.get(keyID, PersistentDataType.STRING);

                    HavaItems havaItem = loadItem(uniqueName);
                    if (havaItem != null) {
                        ItemStack updatedItemStack = GetItemStack(havaItem);
                        inv.setItem(inv.first(itemStack), updatedItemStack);
                    }
                }
            }
        }
    }
    public static void UpdatePlayerInventoryItems(Player player){
        Inventory inv = player.getInventory();

        for (ItemStack itemStack : inv.getContents()) {
            if (itemStack == null) continue;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) continue;

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            NamespacedKey keyID = new NamespacedKey(getPlugin(), HavaNBT.GetItemDonjonID());

            if (container.has(keyID, PersistentDataType.STRING)) {
                String uniqueName = container.get(keyID, PersistentDataType.STRING);

                HavaItems havaItem = loadItem(uniqueName);
                if (havaItem != null) {
                    ItemStack updatedItemStack = GetItemStack(havaItem);
                    inv.setItem(inv.first(itemStack), updatedItemStack);
                }
            }
        }
    }
}