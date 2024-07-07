package pouce.gui.fixedgui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pouce.gui.HavaGuiItem;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.HavaUtilitaireItems;
import pouce.items.spells.HavaSpell;
import pouce.items.spells.utils.HavaSpellUtils;
import pouce.items.utils.HavaItemsUtils;
import pouce.nbt.HavaNBT;

import java.util.*;

import static pouce.HavaPouce.*;

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
        List<HavaMeleeItems> itemsList = HavaItemsUtils.GetMeleeItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {

                HavaMeleeItems meleeItems = itemsList.get(currentIndexItem);

                ItemStack currentItem = meleeItems.getItem().clone();
                ItemMeta currentMeta = currentItem.getItemMeta();

                NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
                PersistentDataContainer containerItemGet = currentMeta.getPersistentDataContainer();
                containerItemGet.set(keyItemGet, PersistentDataType.STRING, meleeItems.getUniqueName());

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
        List<HavaDistanceItems> itemsList = HavaItemsUtils.GetDistanceItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);


        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {

                HavaDistanceItems distanceItems = itemsList.get(currentIndexItem);

                ItemStack currentItem = distanceItems.getItem().clone();
                ItemMeta currentMeta = currentItem.getItemMeta();

                NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
                PersistentDataContainer containerItemGet = currentMeta.getPersistentDataContainer();
                containerItemGet.set(keyItemGet, PersistentDataType.STRING, distanceItems.getUniqueName());

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
        List<HavaUtilitaireItems> itemsList = HavaItemsUtils.GetUtilitaireItems();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);


        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < itemsList.size()) {

                HavaUtilitaireItems utilitaireItems = itemsList.get(currentIndexItem);

                ItemStack currentItem = utilitaireItems.getItem().clone();
                ItemMeta currentMeta = currentItem.getItemMeta();

                NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
                PersistentDataContainer containerItemGet = currentMeta.getPersistentDataContainer();
                containerItemGet.set(keyItemGet, PersistentDataType.STRING, utilitaireItems.getUniqueName());

                currentItem.setItemMeta(currentMeta);
                gui.setItem(i, currentItem);
                currentIndexItem++;
            }
        }
        return gui;
    }

    public static Inventory GetEditItemFixedGui(HavaItems item) {
        Inventory gui = createBorderedGui(5, "§8Edit Items", Material.BLUE_STAINED_GLASS_PANE);
        ItemStack itemStack = HavaItemsUtils.GetItemStack(item);

        if(item instanceof HavaMeleeItems){

            HavaMeleeItems meleeItem = (HavaMeleeItems) item;

            gui.setItem(4, createItem(Material.REDSTONE_TORCH, "§8Modification"));

            NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiEditDonjonItemAction());


            gui.setItem(12, createItem(Material.LECTERN, "§3Nom", item, key, "name"));
            gui.setItem(14, createItem(Material.SPECTRAL_ARROW, "§6Degat (" + meleeItem.getDamage() + ")", item, key, "damage"));

            gui.setItem(21, createItem(Material.OAK_SIGN, meleeItem.getRarity().getFormattedName(), item, key, "rarity"));
            gui.setItem(22, itemStack);
            gui.setItem(23, createItem(Material.TIPPED_ARROW, "§cForce (" + meleeItem.getStrength() + ")", item, key, "strength"));

            gui.setItem(30, createItem(Material.HEART_OF_THE_SEA, "§3NBT"));
            gui.setItem(32, createItem(Material.GLOW_ITEM_FRAME, "§6Abilité", item, key, "ability"));

            ItemStack itemForward = new ItemStack(Material.ARROW);
            ItemMeta meta = itemForward.getItemMeta();


            NamespacedKey keyBack = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonNavAction());
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(keyBack, PersistentDataType.STRING, "backforwardToDonjonItemsMelee");
            meta.setDisplayName("§7Retour");
            itemForward.setItemMeta(meta);

            gui.setItem(36, itemForward);

            gui.setItem(44, createItem(Material.ANVIL, "§4Delete", item, key, "delete"));


        }
        if(item instanceof HavaDistanceItems){
            HavaDistanceItems distanceItem = (HavaDistanceItems) item;

            gui.setItem(4, createItem(Material.REDSTONE_TORCH, "§8Modification"));

            NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiEditDonjonItemAction());


            gui.setItem(12, createItem(Material.LECTERN, "§3Nom", item, key, "name"));
            gui.setItem(14, createItem(Material.SPECTRAL_ARROW, "§6Degat (" + distanceItem.getDamage() + ")", item, key, "damage"));

            gui.setItem(21, createItem(Material.OAK_SIGN, distanceItem.getRarity().getFormattedName(), item, key, "rarity"));
            gui.setItem(22, item.getItem());
            gui.setItem(23, createItem(Material.TIPPED_ARROW, "§cForce (" + distanceItem.getStrength() + ")", item, key, "strength"));

            gui.setItem(30, createItem(Material.HEART_OF_THE_SEA, "§3NBT"));
            gui.setItem(32, createItem(Material.GLOW_ITEM_FRAME, "§6Abilité", item, key, "ability"));


            ItemStack itemForward = new ItemStack(Material.ARROW);
            ItemMeta meta = itemForward.getItemMeta();


            NamespacedKey keyBack = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonNavAction());
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(keyBack, PersistentDataType.STRING, "backforwardToDonjonItemsDistance");
            meta.setDisplayName("§7Retour");
            itemForward.setItemMeta(meta);

            gui.setItem(36, itemForward);

            gui.setItem(44, createItem(Material.ANVIL, "§4Delete", item, key, "delete"));

        }

        return gui;

    }
    public static Inventory GetEditSpellItemFixedGui(HavaItems item) {
        Inventory gui = createBorderedGui(6, "§8Items spell", Material.GRAY_STAINED_GLASS_PANE);

        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiEditDonjonItemAction());

        gui.setItem(4, createItem(Material.GLOW_ITEM_FRAME, "§Items spell"));
        gui.setItem(45, createItem(Material.ARROW, "§7Retour", item, key, "backforwardToDonjonItemsEdit"));

        Map<String, HavaSpell> spellList = HavaSpellUtils.getSpellMap();

        Set<Integer> allowedSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

        List<String> spellNames = new ArrayList<>(spellList.keySet());
        int currentIndexItem = 0;

        for (int i = 0; i < 54; i++) {
            if (allowedSlots.contains(i) && currentIndexItem < spellNames.size()) {

                String spellname = spellNames.get(currentIndexItem);
                HavaSpell spell = spellList.get(spellname);

                ItemStack currentItem = createItem(Material.ITEM_FRAME, "§7" + spellname, item, key, "editspell");
                ItemMeta meta = currentItem.getItemMeta();

                HavaMeleeItems meleeItems = (HavaMeleeItems) HavaItemsUtils.loadItem(item.getUniqueName());

                meta.setLore(spell.getLore());

                if(Objects.equals(spellname, meleeItems.getSpell().getUniqueName())){
                    meta.setDisplayName("§2§n" + spellname);
                    meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                PersistentDataContainer container = meta.getPersistentDataContainer();

                NamespacedKey keySelectedSpell = new NamespacedKey(getPlugin(), HavaNBT.GetGuiDonjonSelectedSpellAction());
                container.set(keySelectedSpell, PersistentDataType.STRING, spellname);
                currentItem.setItemMeta(meta);

                gui.setItem(i, currentItem);

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
        return createItem(material, name, null, null, null);
    }

    private static ItemStack createItem(Material material, String name, HavaItems targetItem, NamespacedKey key, String value) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null && !name.isEmpty()) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (key != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey keyItemGet = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());

            container.set(keyItemGet, PersistentDataType.STRING, targetItem.getUniqueName());
            container.set(key, PersistentDataType.STRING, value);
        }
        item.setItemMeta(meta);
        return item;
    }

}
