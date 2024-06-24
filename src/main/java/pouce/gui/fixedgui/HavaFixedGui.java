package pouce.gui.fixedgui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pouce.items.utils.HavaItemsUtils;

import java.util.List;
import java.util.Set;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaDev;

public class HavaFixedGui {
    public static Inventory GetMeleeItemFixedGui() {
        Inventory gui = createBorderedGui(6, "§7Melee Items", Material.GRAY_STAINED_GLASS_PANE);

        ItemStack itemForward = new ItemStack(Material.ARROW);
        ItemMeta meta = itemForward.getItemMeta();


        NamespacedKey key = new NamespacedKey(getPlugin(), "guiDonjonNavAction");
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "backforwardToDonjonItems");
        meta.setDisplayName("§7Retour");
        itemForward.setItemMeta(meta);

        gui.setItem(4, createItem(Material.DIAMOND_SWORD, "§7Distance"));
        gui.setItem(45, itemForward);
        gui.setItem(53, createItem(Material.REPEATER, ""));
        List<ItemStack> itemsList = HavaItemsUtils.GetMeleeItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {
                gui.setItem(i, itemsList.get(currentIndexItem));
                currentIndexItem++;
            }
        }
        return gui;
    }

    public static Inventory GetDistanceItemFixedGui() {
        Inventory gui = createBorderedGui(6, "§8Distance Items", Material.GRAY_STAINED_GLASS_PANE);

        ItemStack itemForward = new ItemStack(Material.ARROW);
        ItemMeta meta = itemForward.getItemMeta();


        NamespacedKey key = new NamespacedKey(getPlugin(), "guiDonjonNavAction");
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "backforwardToDonjonItems");
        meta.setDisplayName("§7Retour");
        itemForward.setItemMeta(meta);


        gui.setItem(4, createItem(Material.CROSSBOW, "§8Distance"));
        gui.setItem(45, itemForward);
        gui.setItem(53, createItem(Material.REPEATER, ""));
        List<ItemStack> itemsList = HavaItemsUtils.GetDistanceItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);


        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {
                gui.setItem(i, itemsList.get(currentIndexItem));
                currentIndexItem++;
            }
        }
        return gui;
    }
    public static Inventory GetUtilitaireItemFixedGui() {
        Inventory gui = createBorderedGui(6, "§8Distance Items", Material.GRAY_STAINED_GLASS_PANE);

        ItemStack itemForward = new ItemStack(Material.ARROW);
        ItemMeta meta = itemForward.getItemMeta();


        NamespacedKey key = new NamespacedKey(getPlugin(), "guiDonjonNavAction");
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "backforwardToDonjonItems");
        meta.setDisplayName("§7Retour");
        itemForward.setItemMeta(meta);


        gui.setItem(4, createItem(Material.CROSSBOW, "§8Distance"));
        gui.setItem(45, itemForward);
        gui.setItem(53, createItem(Material.REPEATER, ""));
        List<ItemStack> itemsList = HavaItemsUtils.GetUtilitaireItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);


        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {
                gui.setItem(i, itemsList.get(currentIndexItem));
                currentIndexItem++;
            }
        }
        return gui;
    }

    private static Inventory createBorderedGui(int size, String name, Material border) {

        Set<Integer> allowedSlots = Set.of(8, 17, 26, 35, 44, 53);


        Inventory gui = Bukkit.createInventory(null, size*9, name);

        for (int i = 0; i < size * 9; i++) {
            if (i < 10 || i % 9 == 0 || allowedSlots.contains(i) || i > (size-1) * 9) {
                gui.setItem(i, createItem(border, ""));
            }
        }


        return gui;
    }

    private static ItemStack createItem(Material material, String name) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null && !name.isEmpty()) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
        }
        return item;
    }


}
