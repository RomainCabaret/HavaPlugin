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
import pouce.gui.HavaGuiItem;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.utils.HavaItemsUtils;
import pouce.nbt.HavaNBT;

import java.util.List;
import java.util.Set;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaDev;

public class HavaFixedGui {
    public static Inventory GetMeleeItemFixedGui() {
        Inventory gui = createBorderedGui(6, "§7Melee Items", Material.GRAY_STAINED_GLASS_PANE);

        ItemStack itemForward = new ItemStack(Material.ARROW);
        ItemMeta meta = itemForward.getItemMeta();


        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonNavAction());
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
                ItemStack currentItem = itemsList.get(currentIndexItem);
                ItemMeta currentMeta = itemsList.get(currentIndexItem).getItemMeta();

                NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
                PersistentDataContainer containerItemGet = currentMeta.getPersistentDataContainer();
                containerItemGet.set(keyItemGet, PersistentDataType.STRING, currentMeta.getDisplayName());

                currentItem.setItemMeta(currentMeta);
                gui.setItem(i, currentItem);
                currentIndexItem++;
            }
        }
        return gui;
    }

    public static Inventory GetDistanceItemFixedGui() {
        Inventory gui = createBorderedGui(6, "§8Distance Items", Material.GRAY_STAINED_GLASS_PANE);

        ItemStack itemForward = new ItemStack(Material.ARROW);
        ItemMeta meta = itemForward.getItemMeta();


        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonNavAction());
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
                ItemStack currentItem = itemsList.get(currentIndexItem);
                ItemMeta currentMeta = itemsList.get(currentIndexItem).getItemMeta();

                NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
                PersistentDataContainer containerItemGet = currentMeta.getPersistentDataContainer();
                containerItemGet.set(keyItemGet, PersistentDataType.STRING, currentMeta.getDisplayName());

                // Mise à jour de l'ItemMeta
                currentItem.setItemMeta(currentMeta);

                gui.setItem(i, currentItem);

                currentIndexItem++;
            }
        }
        return gui;
    }
    public static Inventory GetUtilitaireItemFixedGui() {
        Inventory gui = createBorderedGui(6, "§dUtilitaire Items", Material.GRAY_STAINED_GLASS_PANE);

        ItemStack itemForward = new ItemStack(Material.ARROW);
        ItemMeta meta = itemForward.getItemMeta();


        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonNavAction());
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "backforwardToDonjonItems");
        meta.setDisplayName("§7Retour");
        itemForward.setItemMeta(meta);


        gui.setItem(4, createItem(Material.END_CRYSTAL, "§dUtilitaire"));
        gui.setItem(45, itemForward);
        gui.setItem(53, createItem(Material.REPEATER, ""));
        List<ItemStack> itemsList = HavaItemsUtils.GetUtilitaireItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);


        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {
                ItemStack currentItem = itemsList.get(currentIndexItem);
                ItemMeta currentMeta = itemsList.get(currentIndexItem).getItemMeta();

                NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
                PersistentDataContainer containerItemGet = currentMeta.getPersistentDataContainer();
                containerItemGet.set(keyItemGet, PersistentDataType.STRING, currentMeta.getDisplayName());

                currentItem.setItemMeta(currentMeta);
                gui.setItem(i, currentItem);
                currentIndexItem++;
            }
        }
        return gui;
    }

    public static Inventory GetEditItemFixedGui(HavaItems item) {
        Inventory gui = createBorderedGui(5, "§8Edit Items", Material.BLUE_STAINED_GLASS_PANE);

        if(item instanceof HavaMeleeItems){

            HavaMeleeItems meleeItem = (HavaMeleeItems) item;

            gui.setItem(4, createItem(Material.REDSTONE_TORCH, "§8Modification"));

            NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiEditDonjonItemAction());


            gui.setItem(12, createItem(Material.LECTERN, "§3Nom", item.getItem(), key, "name"));
            gui.setItem(14, createItem(Material.SPECTRAL_ARROW, "§6Degat (" + meleeItem.getDamage() + ")", item.getItem(), key, "damage"));

            gui.setItem(21, createItem(Material.OAK_SIGN, "§aLore", item.getItem(), key, "lore"));
            gui.setItem(22, item.getItem());
            gui.setItem(23, createItem(Material.TIPPED_ARROW, "§cForce (" + meleeItem.getStrength() + ")", item.getItem(), key, "strength"));

            gui.setItem(30, createItem(Material.HEART_OF_THE_SEA, "§3NBT"));
            gui.setItem(32, createItem(Material.GLOW_ITEM_FRAME, "§6Abilité"));
        }
        if(item instanceof HavaDistanceItems){
            HavaDistanceItems distanceItem = (HavaDistanceItems) item;

            gui.setItem(4, createItem(Material.REDSTONE_TORCH, "§8Modification"));

            NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiEditDonjonItemAction());


            gui.setItem(12, createItem(Material.LECTERN, "§3Nom", item.getItem(), key, "name"));
            gui.setItem(14, createItem(Material.SPECTRAL_ARROW, "§6Degat (" + distanceItem.getDamage() + ")", item.getItem(), key, "damage"));

            gui.setItem(21, createItem(Material.OAK_SIGN, "§aLore", item.getItem(), key, "lore"));
            gui.setItem(22, item.getItem());
            gui.setItem(23, createItem(Material.TIPPED_ARROW, "§cForce (" + distanceItem.getStrength() + ")", item.getItem(), key, "strength"));

            gui.setItem(30, createItem(Material.HEART_OF_THE_SEA, "§3NBT"));
            gui.setItem(32, createItem(Material.GLOW_ITEM_FRAME, "§6Abilité"));
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
        return createItem(material, name, null, null, null);
    }

    private static ItemStack createItem(Material material, String name, ItemStack targetItem, NamespacedKey key, String value) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null && !name.isEmpty()) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (key != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());

            container.set(keyItemGet, PersistentDataType.STRING, targetItem.getItemMeta().getDisplayName());
            container.set(key, PersistentDataType.STRING, value);
        }
        item.setItemMeta(meta);
        return item;
    }

}
