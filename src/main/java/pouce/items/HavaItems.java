package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;

public abstract class HavaItems {
    private String uniqueName;
    private HavaRarity rarity;
    private ItemStack item;

    public HavaItems(String uniqueName, HavaRarity rarity, ItemStack item) {
        this.uniqueName = uniqueName;
        this.rarity = rarity;
        this.item = item;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public HavaRarity getRarity() {
        return rarity;
    }

    public ItemStack getItem() {
        return item;
    }
}
