package pouce.items.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pouce.HavaPouce;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.HavaUtilitaireItems;
import pouce.items.rarity.HavaRarity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HavaItemsUtils {
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
    }

    public static boolean saveItem(HavaItems item) {
        String path = "items." + item.getUniqueName();
        customConfig.set(path + ".rarity", item.getRarity().toString());
        customConfig.set(path + ".item", item.getItem()); // Vous devrez convertir ItemStack en un format sauvegardable

        if (item instanceof HavaMeleeItems) {
            HavaMeleeItems meleeItem = (HavaMeleeItems) item;
            customConfig.set(path + ".donjonType", "melee");
            customConfig.set(path + ".damage", meleeItem.getDamage());
            save();
            return true;
        } else if (item instanceof HavaDistanceItems){
            customConfig.set(path + ".donjonType", "distance");
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
        String path = "items." + uniqueName;
        if (!customConfig.contains(path)) {
            return null;
        }

        HavaRarity rarity = HavaRarity.valueOf(customConfig.getString(path + ".rarity"));
        ItemStack itemStack = customConfig.getItemStack(path + ".item");
        String donjonType = customConfig.getString(path + ".donjonType");

        if ("melee".equalsIgnoreCase(donjonType)) {
            int damage = customConfig.getInt(path + ".damage");
            return new HavaMeleeItems(uniqueName, rarity, itemStack, damage);
        } else if ("distance".equalsIgnoreCase(donjonType)) {
            return new HavaDistanceItems(uniqueName, rarity, itemStack);
        } else if ("utilitaire".equalsIgnoreCase(donjonType)) {
            return new HavaUtilitaireItems(uniqueName, rarity, itemStack);
        }

        return null;
    }


    private static void save() {
        try {
            customConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//  -----------------------------  GET

    public static List<ItemStack> GetMeleeItems() {
        List<ItemStack> meleeItems = new ArrayList<>();
        if (customConfig.contains("items")) {
            Set<String> keys = customConfig.getConfigurationSection("items").getKeys(false);
            for (String key : keys) {
                String path = "items." + key;
                String donjonType = customConfig.getString(path + ".donjonType");
                if ("melee".equalsIgnoreCase(donjonType)) {
                    ItemStack item = customConfig.getItemStack(path + ".item");
                    meleeItems.add(item);
                }
            }
        }
        return meleeItems;
    }
    public static List<ItemStack> GetDistanceItems() {
        List<ItemStack> distanceItems = new ArrayList<>();
        if (customConfig.contains("items")) {
            Set<String> keys = customConfig.getConfigurationSection("items").getKeys(false);
            for (String key : keys) {
                String path = "items." + key;
                String donjonType = customConfig.getString(path + ".donjonType");
                if ("distance".equalsIgnoreCase(donjonType)) {
                    ItemStack item = customConfig.getItemStack(path + ".item");
                    distanceItems.add(item);
                }
            }
        }
        return distanceItems;
    }
    public static List<ItemStack> GetUtilitaireItems() {
        List<ItemStack> utilitaireItems = new ArrayList<>();
        if (customConfig.contains("items")) {
            Set<String> keys = customConfig.getConfigurationSection("items").getKeys(false);
            for (String key : keys) {
                String path = "items." + key;
                String donjonType = customConfig.getString(path + ".donjonType");
                if ("utilitaire".equalsIgnoreCase(donjonType)) {
                    ItemStack item = customConfig.getItemStack(path + ".item");
                    utilitaireItems.add(item);
                }
            }
        }
        return utilitaireItems;
    }
}