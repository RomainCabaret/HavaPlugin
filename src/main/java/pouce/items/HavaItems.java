package pouce.items;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pouce.HavaPouce;
import pouce.gui.HavaGuiItem;

import java.io.File;
import java.io.IOException;

public class HavaItems {
    private String name;
    private ItemStack item;


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
}
