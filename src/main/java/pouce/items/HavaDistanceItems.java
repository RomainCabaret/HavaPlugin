package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;

public class HavaDistanceItems extends HavaItems{

    public HavaDistanceItems(ItemStack item) {
        super(item.getItemMeta().getDisplayName(), HavaRarity.COMMUN, item);
    }

    public HavaDistanceItems(String uniqueName, HavaRarity rarity, ItemStack item) {
        super(uniqueName, rarity, item);
    }


}
