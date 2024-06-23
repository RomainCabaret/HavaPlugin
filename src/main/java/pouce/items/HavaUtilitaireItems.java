package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;

public class HavaUtilitaireItems extends HavaItems{

    public HavaUtilitaireItems(ItemStack item) {
        super(item.getItemMeta().getDisplayName(), HavaRarity.COMMUN, item);
    }
    public HavaUtilitaireItems(String uniqueName, HavaRarity rarity, ItemStack item){
        super(uniqueName, rarity, item);
    }
}
